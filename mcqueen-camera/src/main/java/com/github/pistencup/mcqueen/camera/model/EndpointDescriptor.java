package com.github.pistencup.mcqueen.camera.model;

import lombok.Getter;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class EndpointDescriptor {

    private EndpointDescriptor(){}

    @Getter
    private String nodeIP;

    @Getter
    private String serviceName;

    public static EndpointDescriptor instance(String serviceName){
        EndpointDescriptor descriptor = new EndpointDescriptor();

        descriptor.nodeIP = getLocalIP();
        descriptor.serviceName = serviceName;

        return descriptor;
    }

    private static String getLocalIP(){
        try{
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()){
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() //except loop back addresses: 127.0.0.0 ~ 127.255.255.255
                            && !ip.getHostAddress().contains(":")){

                        return ip.getHostAddress();
                    }
                }
            }
        }catch(Exception e){
            return "";
        }
        return "";
    }

}
