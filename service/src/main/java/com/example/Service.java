package com.example;

import com.example.dao.UserRepository;
import com.example.dto.UserCreateDto;
import com.example.entity.enums.Gender;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
import com.example.interceptor.TransactionInterceptor;
import com.example.mapper.PersonalInfoReadMapper;
import com.example.mapper.UserCreateMapper;
import com.example.mapper.UserReadMapper;
import com.example.service.UserService;
import com.example.util.HibernateUtil;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class Service {

    private final static Common common = new Common();

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            var session = (Session) Proxy.newProxyInstance(
                    SessionFactory.class.getClassLoader(),
                    new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1)
            );
            var userRepository = new UserRepository(session);
            var userReadMapper = new UserReadMapper(new PersonalInfoReadMapper());
            var userCreateMapper = new UserCreateMapper();

            var transactionInterceptor = new TransactionInterceptor(sessionFactory);

            UserService userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreateMapper.class)
                    .newInstance(userRepository, userReadMapper, userCreateMapper);

            UserCreateDto userCreateDto = new UserCreateDto(
                    "qqqq1",
                    "qwerty",
                    "www",
                    "eee",
                    Role.CLIENT,
                    Gender.MALE,
                    UserStatus.ACTIVE
            );
            System.out.println(userService.create(userCreateDto));
        }
    }
}
