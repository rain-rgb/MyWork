package org.jeecg.modules.job.wztaizhangjob.ztyjjob;

import com.alibaba.fastjson.JSONArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Application {
    private static final String accessId = "1v89j0f+TbRqCsjlAwjU";
    private static final String secretKey = "OxB7HrrZfaSdxFytrQ0CocLovjHQqE6QPnQSMMdo";

    private static final String baseUrl = "https://api.mctech.vip";

    private static final String pcrApi = "/mquantity/get-receive-weight-order-list?startId=%d&startVersion=%d&limit=%d&orgId=%d";

    private static Logger logger = LogManager.getLogger("logger");
    private static String outputFile = "project-construction-record.txt";


    private static Object lockObj = new Object();
    // 20线程
    private static int threadCount = 20;
    private static ExecutorService service = Executors.newFixedThreadPool(threadCount);

    private static OpenApiClient client = new OpenApiClient(baseUrl, accessId, secretKey);

    private static AtomicInteger totalCount = new AtomicInteger(0);

    private static final Semaphore sem = new Semaphore(threadCount);

    public static void main(String[] args) throws IOException, InterruptedException {
        File f = new File(outputFile);
        if (f.exists()) {
            f.delete();
        }

//        list.add(1633827216528896L);//对接组织名称：杭州机场高铁站前3标1#钢筋加工场
//        list.add(1633827609351680L);//对接组织名称：杭州机场高铁站前3标拌和站
        List<Long> list = Arrays.asList(1633827216528896L,1633827609351680L);
        for (long id : list) {
            sem.acquire();
            try {
                logger.info("start project: " + id);
                getPcr(id);
            } finally {
                sem.release();
            }
        }

        // 等待所有线程结束
        sem.acquire(threadCount);
    }

    private static void getPcr(long orgId) {
        long startId = 0;
        long startVersion = 0;
        int pageSize = 50;
        while (true) {
            String apiUrl = String.format(pcrApi, startId, startVersion, pageSize, orgId);
            try (RequestResult result = client.get(apiUrl)) {
                // 调用result.GetJsonObject方法可以使用强类型的实体，也可以使用Newtonsoft.json.dll中的JArray类型
                JSONArray arr = (JSONArray) result.getJsonObject();
                System.out.println(arr);
                int count = arr.size();
                totalCount.addAndGet(count);
                WritePcrFile(arr);
                logger.info(String.format("get %d records on project %d", totalCount.get(), orgId));
                if (count < pageSize) {
                    break;
                }

                startId = arr.getJSONObject(arr.size() - 1).getLongValue("id");
                startVersion = arr.getJSONObject(arr.size() - 1).getLongValue("version");
            } catch (Exception ex) {
                logger.info(String.format("call api error:  %s", ex.getMessage()), ex);
            }
        }
    }

    private static void WritePcrFile(JSONArray array)
            throws IOException {
        synchronized (lockObj) {
            try (FileOutputStream stream = new FileOutputStream(outputFile, true)) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, "utf-8"));
                writer.write(array.toString());
                writer.flush();
            }
        }
    }

//    private static List<Long> getProjects() throws IOException {
//        List<Long> list = new ArrayList<>();
//        long startId = 0;
//        long startVersion = 0;
//        int pageSize = 50;
//        while (true) {
//            String apiUrl = String.format(projectsApi, startId, startVersion, pageSize);
//            System.out.println(apiUrl);
//            try (RequestResult result = client.get(apiUrl)) {
//                // 调用result.GetJsonObject方法可以使用强类型的实体，例如ProjectInfo，也可以使用Newtonsoft.json.dll中的JArray类型
//                List<ProjectInfo> projects = result.getList(ProjectInfo.class);
//                for (ProjectInfo proj : projects) {
//                    list.add(proj.getId());
//                }
//                if (projects.size() < pageSize) {
//                    break;
//                }
//                startId = projects.get(projects.size() - 1).getVersion();
//            } catch (MCTechOpenApiException e) {
//                logger.error(e.getMessage(), e);
//                // TODO: 处理异常
//                e.printStackTrace();
//            } catch (MCTechOpenApiRequestException e) {
//                logger.error(e.getMessage(), e);
//                ApiGatewayError error = e.getError();
//                // TODO: 处理api网关返回的异常
//            }
//        }
//        return list;
//    }

}


