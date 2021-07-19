package com.xuyiyi.shouxie.mvcframework.servlet;

import com.xuyiyi.shouxie.mvcframework.annotation.*;
import com.xuyiyi.shouxie.mvcframework.pojo.Handler;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyDispatcherServlet extends HttpServlet {


    private Properties properties = new Properties();


    private List<String> classNames = new ArrayList<>();

    private Map<String, Object> ioc = new ConcurrentHashMap<>();

    //key-> url,value->白名单集合
    // 此处定义一个map来存储url访问白名单
    private Map<String, List<String>> userNameWhiteList = new ConcurrentHashMap<>();


    private List<Handler> handlerMapping = new ArrayList<>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Handler handler = getHandler(req);
        resp.setCharacterEncoding("utf-8");
        resp.setHeader("Content-Type", "text/html;charset=utf-8");

        if(handler == null) {
            resp.getWriter().write("404 not found");
            return;
        }

        // 参数绑定
        // 获取所有参数类型数组，这个数组的长度就是我们最后要传入的args数组的长度
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();


        // 根据上述数组长度创建一个新的数组（参数数组，是要传入反射调用的）
        Object[] paraValues = new Object[parameterTypes.length];

        // 以下就是为了向参数数组中塞值，而且还得保证参数的顺序和方法中形参顺序一致

        Map<String, String[]> parameterMap = req.getParameterMap();

        // 遍历request中所有参数  （填充除了request，response之外的参数）
        for(Map.Entry<String,String[]> param: parameterMap.entrySet()) {
            // name=1&name=2   name [1,2]
            String paramName = param.getKey();
            String value = StringUtils.join(param.getValue(), ",");  // 如同 1,2


            //对参数进行校验
            if (paramName.equals("name")) {
                Pattern pattern = handler.getPattern();
                String pattern1 = pattern.pattern();
                List<String> userNames = userNameWhiteList.get(pattern1);
                if (null != userNames && userNames.size() > 0) {
                    if (!userNames.contains(value)) {
                        resp.getWriter().write("暂无访问当前url："+pattern1+"权限");
                        return;
                    }
                }
            }


            // 如果参数和方法中的参数匹配上了，填充数据
            if(!handler.getParamMapping().containsKey(param.getKey())) {continue;}

            // 方法形参确实有该参数，找到它的索引位置，对应的把参数值放入paraValues
            Integer index = handler.getParamMapping().get(param.getKey());//name在第 2 个位置

            paraValues[index] = value;  // 把前台传递过来的参数值填充到对应的位置去

        }

        int requestIndex = handler.getParamMapping().get(HttpServletRequest.class.getSimpleName()); // 0
        paraValues[requestIndex] = req;

        int responseIndex = handler.getParamMapping().get(HttpServletResponse.class.getSimpleName()); // 1
        paraValues[responseIndex] = resp;




        // 最终调用handler的method属性
        try {
            handler.getMethod().invoke(handler.getController(),paraValues);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }


    private Handler getHandler(HttpServletRequest request) {
        if (handlerMapping.isEmpty()) {
            return null;
        }
        String requestURI = request.getRequestURI();
        for (Handler handler : handlerMapping) {

            Matcher matcher = handler.getPattern().matcher(requestURI);
            if (!matcher.matches()) {
                continue;
            }
            return handler;
        }
        return null;
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        //1、加载配置文件  springmvc.properties
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        //加载配置
        doLoadConfig(contextConfigLocation);
        //2、扫描注解
        doScan(properties.getProperty("scanPackage"));
        //3、初始化bean对象（实现IOC容器，基于注解）
        //基于classNames缓存的类的全限定名、以及反射技术，完成对象的创建和管理
        doInstance();
        //4、实现依赖注入
        doAutowired();
        //5、构造HandlerMapping，降配置好的url和method建立映射关系
        initHandlerMapping();
        //初始化完成
        System.out.println("Spring mvc 初始化完成");

    }

    /**
     * 构造HandlerMapping
     */
    private void initHandlerMapping() {
        if(ioc.isEmpty()) {return;}
        for(Map.Entry<String,Object> entry: ioc.entrySet()) {
            // 获取ioc中当前遍历的对象的class类型
            Class<?> aClass = entry.getValue().getClass();
            if(!aClass.isAnnotationPresent(MyController.class)) {continue;}
            String baseUrl = "";
            if(aClass.isAnnotationPresent(MyRequestMapping.class)) {
                MyRequestMapping annotation = aClass.getAnnotation(MyRequestMapping.class);
                baseUrl = annotation.value(); // 等同于/demo
            }
            // 获取方法
            Method[] methods = aClass.getMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if(!method.isAnnotationPresent(MyRequestMapping.class)) {continue;}
                // 如果标识，就处理
                MyRequestMapping annotation = method.getAnnotation(MyRequestMapping.class);
                String methodUrl = annotation.value();  // /query
                String url = baseUrl + methodUrl;    // 计算出来的url /demo/query
                // 把method所有信息及url封装为一个Handler
                Handler handler = new Handler(entry.getValue(),method, Pattern.compile(url));
                // 计算方法的参数位置信息  // query(HttpServletRequest request, HttpServletResponse response,String name)
                Parameter[] parameters = method.getParameters();
                for (int j = 0; j < parameters.length; j++) {
                    Parameter parameter = parameters[j];
                    if(parameter.getType() == HttpServletRequest.class || parameter.getType() == HttpServletResponse.class) {
                        // 如果是request和response对象，那么参数名称写HttpServletRequest和HttpServletResponse
                        handler.getParamMapping().put(parameter.getType().getSimpleName(),j);
                    }else{
                        handler.getParamMapping().put(parameter.getName(),j);  // <name,2>
                    }
                }
                // 建立url和method之间的映射关系（map缓存起来）
                handlerMapping.add(handler);
                //对@Mysecurity注解进行处理
                if (method.isAnnotationPresent(MySecurity.class)) {
                    MySecurity mySecurityAnnotation = method.getAnnotation(MySecurity.class);
                    String[] value = mySecurityAnnotation.value();
                    if (value != null) {
                        List<String> whiteList = Arrays.asList(value);
                        userNameWhiteList.put(url, whiteList);
                    }
                }
            }
        }


    }

    /**
     * 自动注入
     */
    private void doAutowired() {

        if (ioc.size() == 0) {
            return;
        }
        //遍历ioc容器中所有对象
        for (Map.Entry<String, Object> bean : ioc.entrySet()) {
            //获取对象所有的字段,进行依赖注入
            //getDeclaredFields 是获取类的所有字段,不包括父类
            //getFields 获取所有字段 包括父类
            Field[] declaredFields = bean.getValue().getClass().getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                Field declaredField = declaredFields[i];
                boolean annotationPresent = declaredField.isAnnotationPresent(MyAutowired.class);
                if (!annotationPresent) {
                    continue;
                }
                MyAutowired annotation = declaredField.getAnnotation(MyAutowired.class);
                String beanName = annotation.value();
                if ("".equals(beanName.trim())) {
                    beanName = declaredField.getType().getName();
                }
                //开启赋值
                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    /**
     * 初始化bean对象（实现IOC容器，基于注解）
     */
    private void doInstance() {
        if(classNames.size() == 0) return;

        try{

            for (int i = 0; i < classNames.size(); i++) {
                String className =  classNames.get(i);  //

                // 反射
                Class<?> aClass = Class.forName(className);
                // 区分controller，区分service'
                if(aClass.isAnnotationPresent(MyController.class)) {
                    // controller的id此处不做过多处理，不取value了，就拿类的首字母小写作为id，保存到ioc中
                    String simpleName = aClass.getSimpleName();// DemoController
                    String lowerFirstSimpleName = lowerFirst(simpleName); // demoController
                    Object o = aClass.newInstance();
                    ioc.put(lowerFirstSimpleName,o);
                }else if(aClass.isAnnotationPresent(MyService.class)) {
                    MyService annotation = aClass.getAnnotation(MyService.class);
                    //获取注解value值
                    String beanName = annotation.value();

                    // 如果指定了id，就以指定的为准
                    if(!"".equals(beanName.trim())) {
                        ioc.put(beanName,aClass.newInstance());
                    }else{
                        // 如果没有指定，就以类名首字母小写
                        beanName = lowerFirst(aClass.getSimpleName());
                        ioc.put(beanName,aClass.newInstance());
                    }


                    // service层往往是有接口的，面向接口开发，此时再以接口名为id，放入一份对象到ioc中，便于后期根据接口类型注入
                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (int j = 0; j < interfaces.length; j++) {
                        Class<?> anInterface = interfaces[j];
                        // 以接口的全限定类名作为id放入
                        ioc.put(anInterface.getName(),aClass.newInstance());
                    }
                }else{
                    continue;
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 首字母小写
     * @param str
     * @return
     */
    private String lowerFirst(String str) {
        char[] chars = str.toCharArray();
        if('A' <= chars[0] && chars[0] <= 'Z') {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }

    /**
     * 扫描注解
     * scanPackage: com.xuyiyi.shouxie.mvcframework
     */
    private void doScan(String scanPackage) {
        String scanPackagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + scanPackage.replaceAll("\\.", "/");
        File pack = new File(scanPackagePath);

        File[] files = pack.listFiles();

        for(File file: files) {
            if(file.isDirectory()) { // 子package
                // 递归
                doScan(scanPackage + "." + file.getName());  // com.lagou.demo.controller
            }else if(file.getName().endsWith(".class")) {
                String className = scanPackage + "." + file.getName().replaceAll(".class", "");
                classNames.add(className);
            }

        }


    }

    /**
     * 加载配置文件
     * @param config
     */
    private void doLoadConfig(String config) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(config);
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
