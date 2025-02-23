package top.imwonder.mcauth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.imwonder.mcauth.pojo.UUIDTex;
import top.imwonder.mcauth.pojo.WebTexture;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/***
方法名：skinAPI
 传参：①玩家名字 ②系统分配（假）UUID
 返回：皮肤材质JSON 或者 NULL
 */

public class SkinAPIUtil {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static String skinAPI(String username, String pirateUUid) throws JsonProcessingException {
        String UUidJSON = getAuthorizedUUid(username);
        UUIDTex tex = objectMapper.readValue(UUidJSON, UUIDTex.class);
        String val = UUidJSON != null ? tex.getId() : null;


        if (val != null){
            //名字在正版 查出正版uuid 获取正版材质信息
            WebTexture webt = objectMapper.readValue(getAuthorizedSkin(val), WebTexture.class);
            return webt.getProperties().getValue();
        }else {//名字不在正版 查出盗版uuid对应的材质信息
            return getPirateSkin(pirateUUid).getProperties().getName();
        }
    }
    /**
     * Java8中的Base64解码
     * @param str
     * @return
     */
    public static String decodeByJava8(String str) {
        byte[] result = Base64.getDecoder().decode(str.getBytes());
        String s = null;
        try {
            s = new String(result, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }
    //获取正版UUID
    public static String getAuthorizedUUid(String username){
        return APIGetJSON("https://api.mojang.com/users/profiles/minecraft/"+username);
    }

    //通过正版UUID获取正版皮肤
    public static String getAuthorizedSkin(String UUID){
        return APIGetJSON("https://sessionserver.mojang.com/session/minecraft/profile/"+UUID);
    }

    //通过盗版UUID循环访问网站获取皮肤
    public static WebTexture getPirateSkin(String UUID) throws JsonProcessingException {
        String[] HttpList = {"https://sessionserver.mojang.com/session/minecraft/profile/"+ UUID,
                "https://littleskin.cn/api/yggdrasil/sessionserver/session/minecraft/profile/"+UUID};
        String getSkin = null;
        WebTexture webt = null;
        int HttpNum = 0;
        while(getSkin == null && HttpNum < 2){

            getSkin = APIGetJSON(HttpList[HttpNum]);
            webt = objectMapper.readValue(getSkin, WebTexture.class);
            System.out.println(webt.getProperties().getValue());
            HttpNum++;
        }
        return webt;
    }

    //访问API获取JSON的通用方法
    public static String APIGetJSON(String myUrl) {
        try {
            HttpURLConnection connection;
            InputStream is;
            BufferedReader br;

            URL url = new URL(myUrl);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(30000);
            connection.connect();

            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder sbf = new StringBuilder();
                String temp;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                }

                String json = sbf.toString();
                br.close();
                is.close();
                connection.disconnect();

                return json;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}

