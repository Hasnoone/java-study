package com.xuyiyi.shouxie.mvcframework.servlet;

import com.xuyiyi.shouxie.mvcframework.annotation.MyAutowired;
import com.xuyiyi.shouxie.mvcframework.annotation.MyController;
import com.xuyiyi.shouxie.mvcframework.annotation.MyRequestMapping;
import com.xuyiyi.shouxie.mvcframework.annotation.MyService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MyDispatcherServlet extends HttpServlet {


    private Properties properties = new Properties();


    private List<String> classNames = new ArrayList<>();

    private Map<String, Object> ioc = new ConcurrentHashMap<>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //1、加载配置文件  springmvc.properties
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
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

        System.out.println("Spring mvc 初始化完成");

    }

    /**
     * 构造HanlderMapping
     */
    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }
        String baseUrl = "";
        for (Map.Entry<String, Object> bean : ioc.entrySet()) {
            Class<?> clazz = bean.getValue().getClass();
            boolean isController = clazz.isAnnotationPresent(MyController.class);
            if (!isController) {
                continue;
            }
            boolean isRequestMapping = clazz.isAnnotationPresent(MyRequestMapping.class);
            if (isRequestMapping) {
                MyRequestMapping annotation = clazz.getAnnotation(MyRequestMapping.class);
                String value = annotation.value();//获取到controller上的 requestmapping的赋值 @MyRequestMapping("/demo")  /demo
                baseUrl += value;
            }

            //获取方法
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (!clazz.isAnnotationPresent(MyRequestMapping.class)) {
                    continue;
                }

                MyRequestMapping annotation = method.getAnnotation(MyRequestMapping.class);
                String value = annotation.value();
                baseUrl += value;


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

        if (classNames.size() == 0) {
            return;
        }

        for (int i = 0; i < classNames.size(); i++) {
            String className = classNames.get(i);
            //反射
            try {
                Class<?> clazz = Class.forName(className);
                //区分controller还是Service
                if (clazz.isAnnotationPresent(MyController.class)) {
                    String simpleName = clazz.getSimpleName();//MyController
                    //首字母小写
                    String lowerFirstSimpleName = lowerFirst(simpleName);//myController
                    Object o = clazz.newInstance();
                    ioc.put(lowerFirstSimpleName, o);
                } else if (clazz.isAnnotationPresent(MyService.class)) {
                    MyService annotation = clazz.getAnnotation(MyService.class);
                    String beanName = annotation.value();
                    if (!"".equals(beanName)) {
                        ioc.put(beanName, clazz.newInstance());
                    } else {
                        ioc.put(lowerFirst(clazz.getSimpleName()), clazz.newInstance());
                    }

                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (int i1 = 0; i1 < interfaces.length; i1++) {
                        Class<?> anInterface = interfaces[i1];
                        ioc.put(anInterface.getName(), anInterface.newInstance());
                    }

                } else {
                    continue;
                }


            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }


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
        for (File file : files) {
            if (file.isDirectory()) {
                doScan(scanPackage + "." + file.getName());

            } else if (file.getName().endsWith(".class")) {
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
