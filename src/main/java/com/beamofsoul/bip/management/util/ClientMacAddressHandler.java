package com.beamofsoul.bip.management.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ClientMacAddressUtils
 * @Description 通过UDP方式获取客户端MAC地址的工具类
 * @author MingshuJian
 * @Date 2017年4月12日 上午9:12:00
 * @version 1.0.0
 */
@Slf4j
public class ClientMacAddressHandler {

	private String remoteAddress;  
    private int remotePort = 137;  
    private byte[] buffer = new byte[1024];  
    private DatagramSocket socket = null;  
    
    public ClientMacAddressHandler(String address) throws Exception{  
        remoteAddress = address;  
        socket = new DatagramSocket();  
    }  
  
    private final DatagramPacket send(final byte[] bytes) throws IOException {  
        DatagramPacket packet = new DatagramPacket(bytes,bytes.length, InetAddress.getByName(remoteAddress),remotePort);  
        socket.send(packet);  
        return packet;  
    }  
  
    private final DatagramPacket receive() throws Exception {  
        DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
        try {
			socket.setSoTimeout(2000);
			socket.receive(packet);  
		} catch (Exception e) {
			log.debug("DatagramSocket接收获取MAC地址命令返回值失败，获取行为超时");
		}
        return packet;  
    }  
    private byte[] getQueryCommand() throws Exception {  
        byte[] command = new byte[50];  
        command[0] = 0x00;  
        command[1] = 0x00;  
        command[2] = 0x00;  
        command[3] = 0x10;  
        command[4] = 0x00;  
        command[5] = 0x01;  
        command[6] = 0x00;  
        command[7] = 0x00;  
        command[8] = 0x00;  
        command[9] = 0x00;  
        command[10] = 0x00;  
        command[11] = 0x00;  
        command[12] = 0x20;  
        command[13] = 0x43;  
        command[14] = 0x4B;  
  
        for(int i = 15; i < 45; i++){  
            command[i] = 0x41;  
        }  
        command[45] = 0x00;  
        command[46] = 0x00;  
        command[47] = 0x21;  
        command[48] = 0x00;  
        command[49] = 0x01;  
        return command;  
    }  
    private final String getMacAddress(byte[] packetDataBuffer) throws Exception {  
        // 获取计算机名  
        int i = packetDataBuffer[56] * 18 + 56;  
        String address="";  
        StringBuffer macAddress = new StringBuffer(17);  
        // 先从第56字节位置，读出Number Of Names（NetBIOS名字的个数，其中每个NetBIOS Names Info部分占18个字节）  
        // 然后可计算出“Unit ID”字段的位置＝56＋Number Of Names×18，最后从该位置起连续读取6个字节，就是目的主机的MAC地址。  
        for(int j = 1; j < 7;j++)  
        {  
            address = Integer.toHexString(0xFF & packetDataBuffer[i+j]);  
            if(address.length() < 2)  
            {  
                macAddress.append(0);  
            }  
            macAddress.append(address.toUpperCase());  
            if(j < 6) macAddress.append(':');  
        }  
        return macAddress.toString();  
    }  
  
    private final void close() throws Exception {
    	if (socket != null) {
    		socket.close();  
		}
    }  
  
    public final String getRemoteMacAddress() throws Exception {
        byte[] command = getQueryCommand();
        send(command);
        DatagramPacket packet = receive();
        String macAddress = getMacAddress(packet.getData());
        close();
        return macAddress;
    }
}
