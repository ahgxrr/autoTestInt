package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.UpdateUserInfoCase;
import com.course.model.User;
import com.course.utils.DatabaseUtil;
import netscape.javascript.JSObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class UpdateUserInfoTest {

      @Test(dependsOnGroups = "loginTrue",description = "更改用户信息")
    public void updateUserInfo() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase",1);
        //想将user表中id为2（id为1的updateUserInfoCase的userId）的用户名改为updateUserInfoCase的userName
        System.out.println(updateUserInfoCase.toString());
        System.out.println(TestConfig.updateUserInfoUrl);
        Thread.sleep(2000);
        //从相应接口中得到的结果
        String apires = getResult(updateUserInfoCase);

        //在数据库中修改后返回的结果
        int i = session.update(updateUserInfoCase.getExpected(),updateUserInfoCase);
        User mysqlUser=session.selectOne("getUserInfo",updateUserInfoCase);
     //   JSONObject mysqlJsonUser=new JSONObject(mysqlUser);
        System.out.println("mysqlUser"+mysqlUser);

        Assert.assertEquals(mysqlUser.toString(),apires.toString());

    }

  /*  @Test(dependsOnGroups = "loginTrue",description = "删除用户")
    public void deleteUser() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase",1);
        System.out.println(updateUserInfoCase.toString());
        System.out.println(TestConfig.updateUserInfoUrl);

        //下边为写完接口的代码
        String apiRes = getResult(updateUserInfoCase);

        Thread.sleep(2000);
        User user = session.selectOne("getUpdateUserInfo",updateUserInfoCase);
        System.out.println(user.toString());

        //应该让操作mysql更新后返回的user与接口返回的user进行比对。
        Assert.assertNotNull(user);
        Assert.assertNotNull(apiJson);
    }*/

    private String getResult(UpdateUserInfoCase updateUserInfoCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.updateUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id",updateUserInfoCase.getUserId());
        param.put("userName",updateUserInfoCase.getUserName());
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
        return result;
    }

    //使用json文件的时候用
    /*@Test(dependsOnGroups = "loginTrue",description = "更改用户信息")
    public void updateUserInfo() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase",1);
        //想将user表中id为2（id为1的updateUserInfoCase的userId）的用户名改为updateUserInfoCase的userName
        System.out.println(updateUserInfoCase.toString());
        System.out.println(TestConfig.updateUserInfoUrl);
        Thread.sleep(2000);
        //从相应接口中得到的结果
        JSONObject apiJsonUser = getResult(updateUserInfoCase);

        //在数据库中修改后返回的结果
        int i = session.update(updateUserInfoCase.getExpected(),updateUserInfoCase);
        User mysqlUser=session.selectOne("getUserInfo",updateUserInfoCase);
        JSONObject mysqlJsonUser=new JSONObject(mysqlUser);
        System.out.println("mysqlJsonUser"+mysqlJsonUser);

        Assert.assertEquals(mysqlJsonUser.toString(),apiJsonUser.toString());

    }

    @Test(dependsOnGroups = "loginTrue",description = "删除用户")
    public void deleteUser() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase",1);
        System.out.println(updateUserInfoCase.toString());
        System.out.println(TestConfig.updateUserInfoUrl);

        //下边为写完接口的代码
         JSONObject apiJson = getResult(updateUserInfoCase);

        Thread.sleep(2000);
        User user = session.selectOne("getUpdateUserInfo",updateUserInfoCase);
        System.out.println(user.toString());

        //应该让操作mysql更新后返回的user与接口返回的user进行比对。
        Assert.assertNotNull(user);
        Assert.assertNotNull(apiJson);
    }

    private JSONObject getResult(UpdateUserInfoCase updateUserInfoCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.updateUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id",updateUserInfoCase.getUserId());
        param.put("userName",updateUserInfoCase.getUserName());
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
        JSONObject apiJsonUser=new JSONObject(result);
        System.out.println("getResult里的apiJsonUser："+apiJsonUser);
       // return Integer.parseInt(result);
        return apiJsonUser;
    }*/

}
