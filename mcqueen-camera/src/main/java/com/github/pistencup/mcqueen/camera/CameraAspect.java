package com.github.pistencup.mcqueen.camera;

import com.github.pistencup.mcqueen.camera.config.CameraConfiguration;
import com.github.pistencup.mcqueen.camera.model.ActionRecord;
import com.github.pistencup.mcqueen.camera.model.EndpointDescriptor;
import com.github.pistencup.mcqueen.camera.model.RequestDescriptor;
import com.github.pistencup.mcqueen.camera.model.ResultDescriptor;
import com.github.pistencup.mcqueen.camera.saver.DataSaver;
import com.github.pistencup.mcqueen.racetrack.CloudContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Collection;
import java.util.HashSet;

@Aspect
public class CameraAspect {

    private final EndpointDescriptor endpointDescriptor;
    private final CameraConfiguration cameraConfiguration;
    private final DataSaver dataSaver;
    private static HashSet<String> basePackages = new HashSet<>();

    public CameraAspect(EndpointDescriptor endpointDescriptor, CameraConfiguration cameraConfiguration, DataSaver dataSaver) throws ClassNotFoundException {
        this.endpointDescriptor = endpointDescriptor;
        this.cameraConfiguration = cameraConfiguration;
        this.dataSaver = dataSaver;
    }

    @Pointcut(
            "(!@within(org.springframework.cloud.openfeign.FeignClient)) &&" + //except feign clients
            "(!@annotation(com.github.pistencup.mcqueen.camera.Shadow)) && " + //except Shadow method
            //contains all web action
            "(@annotation(org.springframework.web.bind.annotation.RequestMapping) ||" +
            " @annotation(org.springframework.web.bind.annotation.GetMapping) ||" +
            " @annotation(org.springframework.web.bind.annotation.PostMapping) ||" +
            " @annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
            " @annotation(org.springframework.web.bind.annotation.DeleteMapping) ||" +
            " @annotation(org.springframework.web.bind.annotation.PatchMapping))"
    )
    public void request() {
    }

    private Boolean isUnderAvailablePackage(Class clazz){
        for (String pkg : basePackages){
            if (clazz.getName().startsWith(pkg)){
                return true;
            }
        }

        return false;
    }

    @Around("request()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        if(!isUnderAvailablePackage(joinPoint.getSignature().getDeclaringType())){
            return joinPoint.proceed();
        }
        ActionRecord record = new ActionRecord();
        record.setEndpoint(endpointDescriptor);
        record.setAction(joinPoint.getSignature().toLongString());

        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();

        record.setContext(CloudContext.getCurrent());
        record.setRequest(new RequestDescriptor(attrs, cameraConfiguration));

        record.setBeginTime(System.currentTimeMillis());
        Object result;

        try {
            result = joinPoint.proceed();

            finishActionRecord(attrs, record, result);

            return result;
        } catch (Throwable throwable) {

            finishActionRecord(attrs, record, throwable);

            throw throwable;
        }
    }

    private void finishActionRecord(RequestAttributes attrs, ActionRecord record, Object result) {
        record.setEndTime(System.currentTimeMillis());
        record.setDuration(record.getEndTime() - record.getBeginTime());

        record.setResult(new ResultDescriptor(attrs, result));

        dataSaver.saveData(record);
    }

    static void addBasePackages(Collection<String> pkgs){
        CameraAspect.basePackages.addAll(pkgs);
    }
}
