package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserInfoCase;
import com.course.model.GetUserListCase;
import com.course.model.User;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetUserInfoTest {
 @Test(dependsOnGroups="loginTrue",description = "获取userId为1的用户信息")
    public void getUserInfo() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserInfoCase getUserInfoCase = session.selectOne("getUserInfoCase",1);
        System.out.println(getUserInfoCase.toString());
        System.out.println(TestConfig.getUserInfoUrl);

        //下边为写完接口的代码
        String resUser = getJsonResult(getUserInfoCase);

        Thread.sleep(2000);
        User user = session.selectOne(getUserInfoCase.getExpected(),getUserInfoCase);
 //json对象：{"password":"123456","isDelete":"0","sex":"0","permission":"0","id":1,"userName":"zhangsan","age":"20"}
//json数组：[{"password":"123456","isDelete":"0","sex":"0","permission":"0","id":1,"userName":"zhangsan","age":"20"}]

        System.out.println("从mysql中获得的用户信息"+user.toString());
        System.out.println("调用aqi获得的用户信息"+resUser.toString());
        Assert.assertEquals(user.toString(),resUser.toString());
    }



    private String getJsonResult(GetUserInfoCase getUserInfoCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.getUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id",getUserInfoCase.getUserId());
        //设置请求头信息 设置header
        post.setHeader("content-type","application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //设置cookies
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.store);

        //声明一个对象来进行响应结果的存储
        String resultUser;
        //执行post方法
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        //获取响应结果
        resultUser = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println("调用接口result:"+resultUser);
        return resultUser;
    }

    /* @Test(dependsOnGroups="loginTrue",description = "获取userId为1的用户信息")
    public void getUserInfo() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserInfoCase getUserInfoCase = session.selectOne("getUserInfoCase",1);
        System.out.println(getUserInfoCase.toString());
        System.out.println(TestConfig.getUserInfoUrl);

        //下边为写完接口的代码
        JSONObject apiJson = getJsonResult(getUserInfoCase);

        Thread.sleep(2000);
        User user = session.selectOne(getUserInfoCase.getExpected(),getUserInfoCase);
 //json对象：{"password":"123456","isDelete":"0","sex":"0","permission":"0","id":1,"userName":"zhangsan","age":"20"}
//json数组：[{"password":"123456","isDelete":"0","sex":"0","permission":"0","id":1,"userName":"zhangsan","age":"20"}]
        JSONObject mysqlJson=new JSONObject(user);
        System.out.println("从mysql中获得的用户信息"+mysqlJson.toString());
        System.out.println("调用aqi获得的用户信息"+apiJson.toString());
        Assert.assertEquals(mysqlJson.toString(),apiJson.toString());
    }*/

    //使用moco的时候用
/*    private JSONObject getJsonResult(GetUserInfoCase getUserInfoCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.getUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id",getUserInfoCase.getUserId());
        //设置请求头信息 设置header
        post.setHeader("content-type","application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //设置cookies
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.store);

        //声明一个对象来进行响应结果的存储
        String result;
        //执行post方法
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println("调用接口result:"+result);
        JSONObject jsonObject = new JSONObject(result);
        System.out.println("getJsonResult方法里的JSONObject："+jsonObject.toString());
        return jsonObject;
    }*/
}
