package com.momassistant.utils;

import com.alibaba.fastjson.JSONObject;
import com.momassistant.message.DelayedMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhufeng on 2018/8/27.
 */
public class DelayedMessageSerializer {
    protected static final Log logger = LogFactory.getLog(DelayedMessageSerializer.class);

    private String dir;

    public DelayedMessageSerializer(String dir) {
        this.dir = dir;
    }

    /*
     * 将person对象保存到文件中
     * params:
     * 	p:person类对象
     */
    public void serialize(DelayedMessage p) {
        try {
            //写对象流的对象
            String fiLeName = p.getMessage().toString();
            String fullName = dir + fiLeName;
            File file = new File(fullName);
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(p);                 //将Person对象p写入到oos中
            oos.close();                        //关闭文件流
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
         * 将person对象保存到文件中
         * params:
         * 	p:person类对象
         */
    public List<DelayedMessage> deSerialize() {
        List<DelayedMessage> list = new ArrayList<>();
        try {
            File path = new File(dir);
            File[] files = path.listFiles();
            for (File file : files) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                DelayedMessage delayedMessage = (DelayedMessage) ois.readObject();              //读出对象
                logger.info("deSerialize：" + JSONObject.toJSON(delayedMessage.getMessage()));
                list.add(delayedMessage);
                ois.close();
            }                      //关闭文件流
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}
