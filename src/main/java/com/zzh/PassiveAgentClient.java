package com.zzh;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassiveAgentClient {
    private InetAddress agentAddress;
    private int port;

    public PassiveAgentClient(InetAddress agentAddress, int port) {
        this.agentAddress = agentAddress;
        this.port = port;
    }

    public Map<String, Object> getValues(List<String> keys) throws Exception {

        Map<String, Object> values = new HashMap<String, Object>();
        for (String key : keys) {
            Socket socket = new Socket(agentAddress, port);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream output = socket.getOutputStream();

            byte[] bytes = (key + "\n").getBytes();
            output.write(bytes);
            String inputLine = input.readLine();
            socket.close();

            if (inputLine.length() >= 4 && inputLine.substring(0, 4).equals("ZBXD")) {
                inputLine = inputLine.substring(13, inputLine.length());
            }

            try {
                long inputLong = Long.parseLong(inputLine);
                values.put(key, inputLong);

            } catch (Exception e) {
                try {
                    float inputFloat = Float.parseFloat(inputLine);
                    values.put(key, inputFloat);

                } catch (Exception e2) {
                    values.put(key, inputLine);
                }
            }
        }

        return values;
    }
}
