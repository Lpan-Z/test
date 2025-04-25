package FaceApp.java.service;


import FaceApp.java.constant.Constant;
import FaceApp.java.util.FaceUtil;
import FaceApp.java.util.FileUtil;
import com.aliyun.facebody20191230.Client;
import com.aliyun.facebody20191230.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaModel;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * @Author 12629
 * @Description：
 */
@Service
public class FaceServiceImpl {

    @Autowired
    FaceUtil faceUtil;

    @Autowired
    FileUtil fileUtil;


    public String generateHumanAnimeStyle(MultipartFile file,String style) throws Exception {

        //1.存储照片到指定目录
        File destFile = fileUtil.storageFile(file);


        //这里往后 都是阿里云的代码
        Client client = faceUtil.createClient(Constant.ACCESS_KEY_ID, Constant.ACCESS_KEY_SECRET);


        InputStream inputStream = new FileInputStream(destFile);


        GenerateHumanAnimeStyleAdvanceRequest generateHumanAnimeStyleAdvanceRequest = new GenerateHumanAnimeStyleAdvanceRequest()
                .setImageURLObject(inputStream)
                .setAlgoType(style);

        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            GenerateHumanAnimeStyleResponse response = client.generateHumanAnimeStyleAdvance(generateHumanAnimeStyleAdvanceRequest, runtime);

            // 获取整体结果
            System.out.println("人物动漫化处理结果："+com.aliyun.teautil.Common.toJSONString(TeaModel.buildMap(response)));

            return analysisGenerateHumanAnimeStyleResponse(response);//处理之后的新的图片

        } catch (TeaException error) {
            // 获取整体报错信息
            System.out.println(com.aliyun.teautil.Common.toJSONString(error));
            // 获取单个字段
            System.out.println(error.getCode());
        }

        return "fail";
    }

    private String analysisGenerateHumanAnimeStyleResponse(GenerateHumanAnimeStyleResponse resp) {
        return resp.getBody().getData().imageURL;
    }

    /**
     * 检测是否为活体
     * @param file
     * @return
     */
    public boolean isLivingFace(MultipartFile file) throws Exception {
        //1. 先把上传的文件存储起来
        File destFile = fileUtil.storageFile(file);


        //2.阿里云官方代码
        Client client = faceUtil.createClient(Constant.ACCESS_KEY_ID,Constant.ACCESS_KEY_SECRET);


        InputStream tasks0inputStream = new FileInputStream(destFile);

        DetectLivingFaceAdvanceRequest.DetectLivingFaceAdvanceRequestTasks tasks0 = new DetectLivingFaceAdvanceRequest.DetectLivingFaceAdvanceRequestTasks()
                .setImageURLObject(tasks0inputStream);

        DetectLivingFaceAdvanceRequest advanceRequest = new DetectLivingFaceAdvanceRequest()
                .setTasks(java.util.Arrays.asList(
                        tasks0
                ));
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            DetectLivingFaceResponse resp = client.detectLivingFaceAdvance(advanceRequest, runtime);

            System.out.println("人脸活体检测结果分析："+com.aliyun.teautil.Common.toJSONString(TeaModel.buildMap(resp)));

            //解析resp的内容，并且判断是不是活体
            return analysisDetectLivingFaceResponse(resp);


        } catch (TeaException error) {
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
            System.out.println(error);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            Common.assertAsString(error.message);
            System.out.println(_error);
        }
        return false;
    }



    private boolean analysisDetectLivingFaceResponse(DetectLivingFaceResponse resp) {

        DetectLivingFaceResponseBody.DetectLivingFaceResponseBodyDataElements dataElements = resp.getBody().getData().getElements().get(0);

        //1.结果为空的
        if(dataElements == null) {
            System.out.println("获取人脸活体检测数据为空，请检查！");
            return false;
        }

        //2.检测人脸个数 如果不为1 检测失败
        if (dataElements.faceNumber != 1) {
            //照片中的人脸为2个及以上的人，或者没有人 重新拍照上传
            System.out.println("人脸个数有误：" + dataElements.faceNumber);
            return false;
        }

        //3.获取 需要的3个数据
        List<DetectLivingFaceResponseBody.DetectLivingFaceResponseBodyDataElementsResults> results = dataElements.results;
        //检测结果是否为活体？ 活体：normal  翻拍：liveness
        String label = results.get(0).getLabel();
        // 返回该label分类的概率，取值范围[0.00 - 100.00] 数字越大 概率越大
        Float rate = results.get(0).getRate();
        // pass:直接拍摄，可以通过      review:可能来自翻拍       block: 大概率是翻拍的
        String suggestion = results.get(0).getSuggestion();
        if (suggestion.equals("pass") && label.equals("normal") && rate >= 95.0f) {
            System.out.println("人脸检测通过");
            return true;
        }
        System.out.println("人脸检测未通过");
        return false;
    }


}



