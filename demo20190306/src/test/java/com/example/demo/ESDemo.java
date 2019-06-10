package com.example.demo;

import com.example.demo.dao.OTPExpOpAgentDao;
import com.example.demo.entity.OtpExpOpAgent;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

/**
 * @title: AAA
 * @projectName demo
 * @description: TODO
 * @author hadoop
 * @date 2019-6-311:39
 */


@RunWith(SpringRunner.class)
@SpringBootTest
public class ESDemo {
    @Autowired
    OTPExpOpAgentDao oTPExpOpAgentDao;


    public TransportClient getEsClient() throws IOException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "es_cluster").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("10.1.241.172"), 9300));

        return client;
    }

    //从es中查询数据
    @Test
    public void test1() throws  Exception {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "es_cluster").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("10.1.241.172"), 9300));
        //实现数据查询(指定_id查询) 参数分别是 索引名，类型名  id
        GetResponse response = client.prepareGet("otp_exp_op_agent","otp_exp_op_agent","25").execute().actionGet();
        //得到查询出的数据
        System.out.println(response.getSourceAsString());//打印出json数据
        client.close();//关闭客户端

    }

    //插入数据
    @Test
    public void test2() throws IOException {
        //1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "es_cluster").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("10.1.241.172"), 9300));
        //将数据转换成文档的格式（后期可以使用java对象，将数据转换成json对象就可以了）
        XContentBuilder doContentBuilder= XContentFactory.jsonBuilder()
                .startObject()
//                .field("id", "1") //字段名 ： 值
//                .field("title", "java设计模式之装饰模式")
//                .field("content", "在不必改变原类文件和使用继承的情况下，动态地扩展一个对象的功能")
//                .field("postdate", "2018-05-20")
//                .field("url", "https://www.cnblogs.com/chenyuanbo/")

                .field("waybill_no", "waybill111")
                .field("org_code", "210901")
                .field("op_code", "111")
                .field("aux_op_code", "NEW")
                .field("create_time", "2019-06-01")
                .endObject();
        //添加文档  index1:索引名 blog:类型 10:id
//.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE) 代表插入成功后立即刷新，因为ES中插入数据默认分片要1秒钟后再刷新



        IndexResponse response = client.prepareIndex("otp_exp_op_agent","otp_exp_op_agent","10")
                .setSource(doContentBuilder).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).get();
        System.out.println(response.status());
        //打印出CREATED 表示添加成功
    }


    @Test
    public void searchFromDB()  throws IOException{
        List<OtpExpOpAgent> agentList = oTPExpOpAgentDao.getAgentlist();
        TransportClient client = getEsClient();
//        test3 tt = new test3();
        for (OtpExpOpAgent agent : agentList){
            System.out.println("===============" + agent.getWaybill_no());
            test3(client,agent);

        }
    }

    public void test3(TransportClient client,OtpExpOpAgent agent) throws IOException {
        //将数据转换成文档的格式（后期可以使用java对象，将数据转换成json对象就可以了）
        XContentBuilder doContentBuilder= XContentFactory.jsonBuilder()
                .startObject()
                .field("waybill_no", agent.getWaybill_no())
                .field("org_code", agent.getOrg_code())
                .field("op_code", agent.getOp_code())
                .field("aux_op_code",agent.getAux_op_code())
                .field("create_time", agent.getCreate_time().toString())
                .endObject();
        //添加文档  index1:索引名 blog:类型 10:id
//.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE) 代表插入成功后立即刷新，因为ES中插入数据默认分片要1秒钟后再刷新
//        IndexResponse response = client.prepareIndex("otp_exp_op_agent","otp_exp_op_agent",agent.getWaybill_no()+agent.getCreate_time().toString()+agent.getCreate_time().toString())
//                .setSource(doContentBuilder).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).get();

        IndexResponse response = client.prepareIndex("wd_waybill_op_v1","wd_waybill_op",agent.getWaybill_no()+agent.getCreate_time().toString()+agent.getCreate_time().toString())
                .setSource(doContentBuilder).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).get();

        System.out.println(response.status());
        //打印出CREATED 表示添加成功
    }



}

