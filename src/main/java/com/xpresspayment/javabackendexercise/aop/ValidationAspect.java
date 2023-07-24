package com.xpresspayment.javabackendexercise.aop;

import com.xpresspayment.javabackendexercise.exception.customException.UnauthorizedException;
import com.xpresspayment.javabackendexercise.payload.request.CreateUserRequest;
import com.xpresspayment.javabackendexercise.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ValidationAspect {
    @Autowired
    private UserService userService;

    @Pointcut(value = "execution(* com.xpresspayment.javabackendexercise.controller.UserController.registerUser(..))")
    public void checkThatUserWithEmailDoesNotExist(){}

    @Before(value = "checkThatUserWithEmailDoesNotExist()")
    public void validateEmailInCreateUserRequest(JoinPoint joinPoint){
        String email = ((CreateUserRequest) joinPoint.getArgs()[0]).email();
        if(userService.checkIfUserWithEmailExists(email)){
            throw new UnauthorizedException("User with email already exists");
        }
    }


}
