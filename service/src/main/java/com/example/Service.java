package com.example;

import com.example.dao.MediaItemRepository;
import com.example.dao.UserRepository;
import com.example.dto.UserCreateDto;
import com.example.entity.MediaItem;
import com.example.entity.User;
import com.example.entity.enums.Gender;
import com.example.entity.enums.MediaItemType;
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
import java.util.Optional;

public class Service {

    private final static Common common = new Common();

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
        }
    }
}
