package org.jeecg.modules.job.Licangjob;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * 获取料仓仓位定时任务(中铁三局西康)
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LicangJob implements Job {

    @Autowired
    private  IWzliaocangService liaocangService;
    private static final String dataurl = "http://web.iot.ctrlcloud.cn/accessApi/getSiloList";
    private HashMap<String, Object> paramMap = new HashMap<String, Object>() {{
            put("userName", "xaztsj");
            put("password", "123456");
        }
    };


    private static final HashMap<String, String> licangMap = new HashMap<String, String>() {{

        put("lv202107418", "dc0e4cf0-1a52-4c1b-a019-259565f9d7e5");//粉煤灰1-1
        put("lv202107281", "a9aa1009-652b-4439-bdbf-445eecb9d39b");//粉煤灰1-2
        put("lv202107235", "0fa767a0-5d8a-48e5-b213-c10d43ec179f");//粉煤灰1-3
        put("lv202107318", "28cc7e08-c1da-4382-a92e-fad9b6dab579");//粉煤灰1-4
        put("lv202107285", "445c540b-cbe5-48a9-906b-d6f0d571a5a3");//水泥1-1
        put("lv202107017", "672c6d9f-dbf8-4ff1-a8b2-99f9a75e8e0a");//水泥1-2
        put("lv202107400", "da3617d0-1e25-4398-a11a-115e62df081a");//水泥1-3
        put("lv202107420", "5f75d958-9ce2-4c22-b2f6-67db9df70d8d");//水泥1-4
        put("lv202107016", "d0228e5e-e503-459d-b0bc-8c8ccd00aef2");//水泥1-5
        put("lv202107419", "763d31e6-aab2-4551-a1c0-c9fe12bea266");//水泥1-6
        put("lv202107009", "69f84078-bd08-4176-91cf-eeec0ca808f8");//水泥1-7
        put("lv202107431", "facb7c95-6e21-4f23-b23a-2cd63d131040");//水泥1-8
        put("lv202107280", "ba5d2716-778d-4e9b-aff2-71b75674d176");//粉煤灰2-1
        put("lv202107430", "7e88da03-b509-4824-9303-5ef3ec534fc4");//粉煤灰2-2
        put("lv202107006", "2fad0f32-2c3d-4f62-a434-5c57775cbbb4");//粉煤灰2-3
        put("lv202107414", "18647a47-819e-4d7d-b5f7-03440fd43536");//粉煤灰2-4
        put("lv202107411", "9c05fcf2-3b8f-40b1-a19f-750e7d37350c");//水泥2-1
        put("lv202107001", "202c2d5a-061a-4283-b986-f74fe88176b2");//水泥2-2
        put("lv202107427", "d5edb2d8-de1d-4475-a758-88aa4bc39960");//水泥2-3
        put("lv202112300", "50353f5a-b705-4fcc-888c-1fee8798969a");//水泥2-4
        put("lv202107416", "de91b18b-4d81-471a-96ed-309af4febf8c");//水泥2-5
        put("lv202107402", "385cc50e-bb1e-4647-a95e-13c96b9729c2");//水泥2-6
        put("lv202107406", "8e698296-9034-48cc-9b17-acd8c319b903");//水泥2-7
        put("lv202107409", "b19d9ea2-a173-4a8d-bc54-4133c795cf21");//水泥2-8

        put("lv202112305", "fb9fc95a-5d3d-4303-aac7-2366cb0fd6c5");//粉煤灰2-1
        put("lv202112314", "15cc08d3-01a2-406e-b9b3-f4f2fe7e66c4");//粉煤灰2-2
        put("lv202112322", "af42d8ee-a49e-4c81-bb07-9435d92d551d");//粉煤灰2-3
        put("lv202112317", "f2f57a40-860c-407b-8fce-2be29d45e28a");//粉煤灰2-4
        put("lv202112307", "e456529f-3d60-49b3-bf0b-b7bb5ec3f2ee");//水泥2-1
        put("lv202112329", "d600cc2f-4e02-4726-a54f-d199afa19aa2");//水泥2-2
        put("lv202112303", "30d6fe33-5e6f-49bb-af59-6f3a01219ecd");//水泥2-3
        put("lv202112304", "7df2ada4-d47c-4d01-bb3e-5df4801e5615");//水泥2-4
        put("lv202112336", "8e76e225-1e8a-49c3-a25e-66f27f8d0a96");//水泥2-5
        put("lv202112308", "87bbdc92-dca7-463f-991c-9b47503a8c1d");//水泥2-6
        put("lv202112323", "e37ef2a7-b61b-4271-ac94-e1a505216082");//水泥2-7
        put("lv202112301", "1964c0c7-aeb9-4e81-b40e-023545e0e1f8");//水泥2-8

        put("lv202112311", "01bd80fa-6809-4055-8078-c9f146fcbea3");//粉煤灰1-1
        put("lv202112309", "17fba38a-f094-4544-b3b8-d9a91a123af2");//粉煤灰1-2
        put("lv202112302", "0cbc0577-38a7-492c-bbf8-cf58406e413c");//粉煤灰1-3
        put("lv202112306", "b39a73f6-4e3f-4dd6-ba15-443eb511d127");//粉煤灰1-4
        put("lv202112312", "ae891340-4cda-400f-be80-a4330ab4a98c");//水泥1-1
        put("lv202112310", "b6246285-eae5-44c3-9018-8e9b76da4a87");//水泥1-2
        put("lv202112340", "8d94290b-ddb5-451c-b4bc-7ddfc1fcabe2");//水泥1-3
        put("lv202112319", "676aabff-cfd0-4293-b409-4d6632914791");//水泥1-4
        put("lv202112320", "a0715f14-d775-4029-abe6-f16b0d4a7329");//水泥1-5
        put("lv202112318", "ee898b45-fe97-45bc-b6ea-75d17dcb1351");//水泥1-6
        put("lv202112316", "33d30d45-cb98-4183-b4d6-0d6af061d7d6");//水泥1-7
        put("lv202112321", "5dd38dd9-fe6b-428d-86b7-d577bf04bbbd");//水泥1-8

        put("lv202112443", "8c1ad6b9-0996-474b-9b45-5fd389bdcdm1");//1-1粉煤灰
        put("lv202112437", "8c1ad6b9-0996-474b-9b45-5fd389bdcdm2");//1-2粉煤灰
        put("lv202112456", "8c1ad6b9-0996-474b-9b45-5fd389bdcdm3");//1-3粉煤灰
        put("lv202112450", "8c1ad6b9-0996-474b-9b45-5fd389bdcdm4");//1-4粉煤灰
        put("lv202112427", "677ce0cd-5efc-4316-b1ba-a875905cd1b1");//1-1水泥
        put("lv202112430", "677ce0cd-5efc-4316-b1ba-a875905cd1b2");//1-2水泥
        put("lv202112440", "677ce0cd-5efc-4316-b1ba-a875905cd1b3");//1-3水泥
        put("lv202112460", "677ce0cd-5efc-4316-b1ba-a875905cd1b4");//1-4水泥
        put("lv202112473", "677ce0cd-5efc-4316-b1ba-a875905cd1b5");//1-5水泥
        put("lv202112471", "677ce0cd-5efc-4316-b1ba-a875905cd1b6");//1-6水泥
        put("lv202112451", "677ce0cd-5efc-4316-b1ba-a875905cd1b7");//1-7水泥
        put("lv202112465", "677ce0cd-5efc-4316-b1ba-a875905cd1b8");//1-8水泥

        put("lv202112457", "8c1ad6b9-0996-474b-9b45-5fd389bdcdd3");//2-1粉煤灰
        put("lv202112429", "dc6693bc-0ebf-4550-b496-2e216c28ea03");//2-2粉煤灰
        put("lv202112447", "9f103d98-d7b4-45b1-808a-1e1311a8d1ae");//2-3粉煤灰
        put("lv202112428", "901e7431-f126-4ec6-bd82-1a2709e8588f");//2-4粉煤灰
        put("lv202112448", "bd61905f-f7c4-493a-a388-6ee8926f322f");//2-1水泥
        put("lv202112452", "f6dde868-03a4-429e-89c9-15b7ce8b3b00");//2-2水泥
        put("lv202112442", "16ddde8f-addd-47a7-b717-87f96430a3a0");//2-3水泥
        put("lv202112480", "9f9a2d44-24a6-4cf7-bee1-01b3d0be5c91");//2-4水泥
        put("lv202112436", "5e54786e-935b-46a6-af44-2e91ee9b1868");//2-5水泥
        put("lv202112445", "063862c9-7451-41a8-a506-4967b4e97be2");//2-6水泥
        put("lv202112446", "fc653382-c6b2-452f-9967-cbfbbecb9c91");//2-7水泥
        put("lv202112479", "677ce0cd-5efc-4316-b1ba-a875905cd1bc");//2-8水泥

        put("lv202112566", "8bb7fe16-b53d-415d-9ee6-47cace5211a3");//1-1粉煤灰
        put("lv202112511", "cb16fda8-e13b-44f0-8d58-0823dd7e1fa5");//1-2粉煤灰
        put("lv202112488", "f8d8ad1a-bd46-4e07-b9db-2cadcd2bf0a3");//1-3粉煤灰
        put("lv202112536", "c7946725-6944-40e7-b604-b35dbe70443f");//1-4粉煤灰
        put("lv202112565", "d6437a4e-1e7c-4244-a02b-5562312caab0");//1-1水泥
        put("lv202112564", "08a6d1c1-ea32-443c-9827-e83dbb003122");//1-2水泥
        put("lv202112532", "07dfffc7-f5df-40c9-b8c7-39d8af742ff8");//1-3水泥
        put("lv202112551", "799a0031-eba7-44ab-b789-c025613eb4ed");//1-4水泥
        put("lv202112560", "6fb4ccb7-691c-4b41-a70a-ad7774421fed");//1-5水泥
        put("lv202112546", "203b6279-1e88-4ddb-8587-5923cfe6b409");//1-6水泥
        put("lv202112562", "ef1149df-5176-4018-a6da-770e955e911b");//1-7水泥
        put("lv202112550", "2ea22f6b-d09b-45d7-b858-50773b66c5a1");//1-8水泥
        put("lv202112540", "5af3905a-1085-481b-bc4a-7ed8395acd29");//2-1粉煤灰
        put("lv202112486", "979f4333-1c37-4837-a188-12be77a796c4");//2-2粉煤灰
        put("lv202112542", "17c22a9b-0a02-4433-a1dc-6b632210df6b");//2-3粉煤灰
        put("lv202112561", "07dfdaee-9cc8-419e-b2ad-9c5071138e96");//2-4粉煤灰
        put("lv202112556", "f9a8cb94-2c99-4005-9e7f-e34f4e4fa6c9");//2-1水泥
        put("lv202112563", "f9959800-4ab3-468c-9cdb-5dc4ec0ef9f2");//2-2水泥
        put("lv202112490", "71e9693c-9c6a-4d28-a386-1f01eed931ab");//2-3水泥
        put("lv202112498", "8de34e5b-f9e9-42b9-97f0-8065c66ecd97");//2-4水泥
        put("lv202112544", "721de120-a99d-4eb3-87c3-b714e9962450");//2-5水泥
        put("lv202112474", "4d8e1cf6-2e28-4a56-9bde-e9783b794a8d");//2-6水泥
        put("lv202112538", "50599e2c-cb1f-44dc-aa7b-e4e36bdd2665");//2-7水泥
        put("lv202112523", "f71e28ad-867f-4294-a1cd-0d807f58fd67");//2-8水泥

        put("lv202203113", "bd78a0cf-74b7-41b5-92c7-d738c414a523");//粉煤灰1
        put("lv202203111", "bd78a0cf-74b7-41b5-92c7-d738c414a5231");//粉煤灰2
        put("lv202203125", "bd78a0cf-74b7-41b5-92c7-d738c414a5232");//粉煤灰3
        put("lv202203197", "a9bec729-f7bf-47dc-90d0-af5538683788");//水泥1
        put("lv202203188", "17540fbc-93d6-42b4-9110-b28664ee50da");//水泥2
        put("lv202203128", "2976c98b-0a4a-4a9a-91f8-5472590579bb");//水泥3
        put("lv202203192", "10f5a95e-b0e0-4d26-a1ad-03456757026c");//水泥4
        put("lv202203126", "7fd24fba-abda-47dd-b6db-5b50b3fdafc6");//水泥5
        put("lv202203120", "d9c35a91-f774-4407-99fb-b2d38726cd4f");//水泥6

        put("lv202203105", "bd78a0cf-74b7-41b5-92c7-d738c414a5233");//粉煤灰1
        put("lv202203102", "bd78a0cf-74b7-41b5-92c7-d738c414a5234");//粉煤灰2
        put("lv202203049", "bd78a0cf-74b7-41b5-92c7-d738c414a5235");//粉煤灰3
        put("lv202203060", "d9c35a91-f774-4407-99fb-b2d38726cd4f6");//水泥1
        put("lv202203058", "d9c35a91-f774-4407-99fb-b2d38726cd4f5");//水泥2
        put("lv202203048", "d9c35a91-f774-4407-99fb-b2d38726cd4f4");//水泥3
        put("lv202112165", "d9c35a91-f774-4407-99fb-b2d38726cd4f3");//水泥4
        put("lv202112140", "d9c35a91-f774-4407-99fb-b2d38726cd4f2");//水泥5
        put("lv202112133", "d9c35a91-f774-4407-99fb-b2d38726cd4f1");//水泥6

        put("lv202203056", "5ccd9a91-225e-429d-a555-87c615c21516");//1#矿粉
        put("lv202203067", "918672e1-b773-40cc-94b9-c6a058416f1e");//2#粉煤灰
        put("lv202203071", "aba9044c-fc6d-4353-9e7e-55509dfa5ca9");//3#水泥
        put("lv202203075", "ec754e47-1fd2-4695-a311-1f8984e19036");//4#水泥
        put("lv202203052", "4b24ad77-950e-4a8d-8168-6bcdf9026b28");//5#水泥

        put("lv202203116", "67879911-ae4e-4c41-978a-396a8a9a0778");//1#矿粉
        put("lv202203127", "45b802b6-a1c1-4363-b1ba-e2dc0dfe13ce");//2#粉煤灰
        put("lv202203064", "ac6b59f8-d7e7-4bf9-a768-3397cdfe2e15");//3#水泥
        put("lv202203055", "5a5b448c-b596-4df8-bed7-55e85e31805e");//4#水泥
        put("lv202203045", "1e0f247f-ddd3-4c63-b176-6ae7b2c75704");//5#水泥

        put("lv202112254", "db2ff617-8dad-41e6-bb1e-f063a5f505fa");//水泥1
        put("lv202112530", "244db5a5-b160-4ff8-8932-7b7598a6b12c");//水泥2
        put("lv202112481", "087f4c89-d8ef-4c32-ba88-963fb3e1270c");//水泥3
        put("lv202112512", "e74ebfe6-f3cc-4f03-9d18-71454ce072eb");//水泥4
        put("lv202112491", "55d3b05b-005c-44cd-944d-03490edb1940");//粉煤灰5
        put("lv202112554", "01c1a2a4-accf-41f5-9c89-8396eb3df319");//矿粉6

        put("lv202111028", "6836396c-c92d-450a-9af7-7c7f19d3ad9c");//水泥1
        put("lv202107228", "1effdbf0-b5f1-4901-88f1-7ee908ca0413");//水泥2
        put("lv202112492", "926162f4-036a-4f5d-816b-a7f24892d4a5");//水泥3
        put("lv202112528", "8d9832e6-9123-4cec-bb7a-8684a49b9810");//水泥4
        put("lv202106053", "ca6f2d6d-eef8-4dcd-b18f-53f2f0dadd58");//粉煤灰5
        put("lv202112483", "d9fb4786-d969-4d46-bb90-5396a9e95da6");//矿粉6

        // 柯诸一标2号拌合站
        put("lv202211390", "9f4d45d8-d7c3-4150-b7cc-09c1a74abf7c");//2-1水泥
        put("lv202211378", "c54ea582-2d89-47ff-8d4a-95c68cacb547");//2-2水泥
        put("lv202211385", "b3574fe5-e908-4e5f-9fd8-8f2c4bc8047e");//2-3水泥
        put("lv202211362", "42664369-6c5a-41a7-a032-f184b315fb49");//2-5粉煤灰
        put("lv202211105", "60c6e2d4-0d23-4541-a0b3-f689bf0b1ae2");//2-4水泥
        put("lv202211365", "e32f8104-2410-4ca6-8ad7-0e9ace66087c");//2-6粉煤灰
        put("lv202211376", "2dd47cab-51bf-442f-9568-40c190af8813");//1-1水泥
        put("lv202211371", "e4144908-1509-45e4-bb15-86f8798910aa");//1-2水泥
        put("lv202211391", "00e3493f-2012-4ab1-b3fd-8710953cdf06");//1-3水泥
        put("lv202211368", "79ca638c-9d1a-4bdc-8120-ef6bde14ba9c");//1-4水泥
        put("lv202211370", "cb864035-db45-44db-aac9-0eb24d0f3069");//1-5粉煤灰
        put("lv202211395", "e28246f9-f71d-4a33-a441-3c29d0b19a65");//1-6粉煤灰

        // 广西中铁一局
        put("lv202205008", "2d603f5f-b4ee-4af8-8fc2-cab3b89ada04");// 水泥1-1
        put("lv202205006", "9fa46585-6b0f-4ec8-a630-1b4ff2e4098b");// 水泥1-2
        put("lv202203011", "e214f673-6c6c-4562-b6af-ee14b21fcc4d");// 水泥1-3
        put("lv202203083", "84e86a97-9db0-4b62-b5d5-78301e030251");// 水泥1-4
        put("lv202203133", "d7c0c59b-bcde-4d3b-8907-a63c588a9e88");// 煤灰1-5
        put("lv202304221", "43baac9a-748a-408b-93dc-cebbb6b2fa09");// 煤灰1-6
        put("lv202205035", "e52ac515-49fc-42b6-9e4f-3ffb4eca9b14");// 水泥2-1
        put("lv202203070", "9b43c168-5d97-423f-bf0a-c7ca1eb22b41");// 水泥2-2
        put("lv202304227", "2da62f3c-f663-4e79-83b5-ac9d5cf32e09");// 水泥2-3
        put("lv202205003", "110d33a8-8540-4def-b425-8ad13260174d");// 水泥2-4
        put("lv202205015", "b219ec0a-b6a6-4ef2-a1bd-2d7ae62ebe23");// 煤灰2-5
        put("lv202205009", "c2c49d4e-0601-4f01-bfe7-a7350f69273b");// 煤灰2-6

//        // 杭州机场高铁
//        put("lv202309019", "906bf1e5-b0f4-42f4-94af-6a2c9841af93");// 1-1水泥
//        put("lv202304237", "f03c494f-a5b8-48f3-9208-8cf9cc5b0c7e");// 1-2水泥
//        put("lv202304347", "c6bb821a-1bd6-4cea-bd64-21912e3ca453");// 1-3水泥
//        put("lv202311017", "8e0d9b62-cdda-4478-afd1-4e4277bde7e7");// 1-4水泥
//        put("lv202304289", "9938213e-0f6d-4349-8da8-133302bdb2f6");// 1-5水泥
//        put("lv202304320", "bc6d0fab-8015-43f5-9915-8269a1cfab3b");// 1-6水泥
//        put("lv202304351", "6482b01d-3549-4732-892c-a72e681ef84d");// 1-1粉煤灰
//        put("lv202304295", "7504f214-ee29-474a-8dcb-9dabfa137d77");// 1-2粉煤灰
//        put("lv202304302", "76d59fd0-cba5-4ccd-965d-c0e281354b1a");// 1-3粉煤灰
//        // 杭州机场高铁
//        put("lv202304288", "8737cc42-8591-4259-a556-a7cf90c62183");// 2-1水泥
//        put("lv202304301", "434a47ca-d2e3-4885-9f72-36ab19bf2dae");// 2-2水泥
//        put("lv202304365", "048f8401-d0a3-40ca-86c7-e4cc6490d210");// 2-3水泥
//        put("lv202304267", "9b21eb78-19f2-48e0-a991-2efd24d8bdda");// 2-4水泥
//        put("lv202304355", "c13a6fb8-38a8-41c6-a581-b1f471e4f043");// 2-5水泥
//        put("lv202304344", "800b2a77-ead5-4792-80ad-5fcd95c8b596");// 2-6水泥
//        put("lv202304337", "ca4fad72-2230-42b3-8925-8319fb410937");// 2-1粉煤灰
//        put("lv202304303", "15bf381b-0a93-47b4-b59d-bcbe2c711aee");// 2-2粉煤灰
//        put("lv202309021", "f03761f0-0c65-4909-ba35-fd6b4de07bec");// 2-3粉煤灰
//        // 杭州机场高铁
//        put("lv202304348", "7f61dc09-b008-4b52-bee0-0b467bc4d501");// 3-1水泥
//        put("lv202304357", "126d4e80-5d75-4fdc-9692-4a13831f58af");// 3-2水泥
//        put("lv202309016", "4e346df0-d7e0-4174-a414-0d8c362eb6da");// 3-3水泥
//        put("lv202304340", "0e79a46d-c8f6-43db-85da-8cd8015bbe14");// 3-4水泥
//        put("lv202304275", "5c8b1332-4e2b-490b-8dfb-90a036f72a31");// 3-5水泥
//        put("lv202304359", "9bb750ae-2a79-4a16-9451-c56bfec87bd7");// 3-6水泥
//        put("lv202304362", "2d09188f-bb8d-4b2b-a957-2f9f82e03ae2");// 3-1粉煤灰
//        put("lv202304309", "d5cf69ef-032c-4f97-a52b-630426efd4a1");// 3-2粉煤灰
//        put("lv202304278", "c1324c26-5406-4fd7-a554-8775640a9e68");// 3-3粉煤灰
//


        // 杭州机场高铁
        put("202403038", "906bf1e5-b0f4-42f4-94af-6a2c9841af93");// 1-1水泥
        put("202403037", "f03c494f-a5b8-48f3-9208-8cf9cc5b0c7e");// 1-2水泥
        put("202403027", "c6bb821a-1bd6-4cea-bd64-21912e3ca453");// 1-3水泥
        put("202403036", "8e0d9b62-cdda-4478-afd1-4e4277bde7e7");// 1-4水泥
        put("202403035", "9938213e-0f6d-4349-8da8-133302bdb2f6");// 1-5水泥
        put("202403030", "bc6d0fab-8015-43f5-9915-8269a1cfab3b");// 1-6水泥
        put("202403024", "6482b01d-3549-4732-892c-a72e681ef84d");// 1-1粉煤灰
        put("202403026", "7504f214-ee29-474a-8dcb-9dabfa137d77");// 1-2粉煤灰
        put("202403023", "76d59fd0-cba5-4ccd-965d-c0e281354b1a");// 1-3粉煤灰
        // 杭州机场高铁
        put("202403029", "8737cc42-8591-4259-a556-a7cf90c62183");// 2-1水泥
        put("202403040", "434a47ca-d2e3-4885-9f72-36ab19bf2dae");// 2-2水泥
        put("202403034", "048f8401-d0a3-40ca-86c7-e4cc6490d210");// 2-3水泥
        put("202403021", "9b21eb78-19f2-48e0-a991-2efd24d8bdda");// 2-4水泥
        put("202403042", "c13a6fb8-38a8-41c6-a581-b1f471e4f043");// 2-5水泥
        put("202403043", "800b2a77-ead5-4792-80ad-5fcd95c8b596");// 2-6水泥
        put("202403022", "ca4fad72-2230-42b3-8925-8319fb410937");// 2-1粉煤灰
        put("202403019", "15bf381b-0a93-47b4-b59d-bcbe2c711aee");// 2-2粉煤灰
        put("202403020", "f03761f0-0c65-4909-ba35-fd6b4de07bec");// 2-3粉煤灰
        // 杭州机场高铁
        put("202403028", "7f61dc09-b008-4b52-bee0-0b467bc4d501");// 3-1水泥
        put("202403095", "126d4e80-5d75-4fdc-9692-4a13831f58af");// 3-2水泥
        put("202403093", "4e346df0-d7e0-4174-a414-0d8c362eb6da");// 3-3水泥
        put("202403092", "0e79a46d-c8f6-43db-85da-8cd8015bbe14");// 3-4水泥
        put("202403094", "5c8b1332-4e2b-490b-8dfb-90a036f72a31");// 3-5水泥
        put("202403091", "9bb750ae-2a79-4a16-9451-c56bfec87bd7");// 3-6水泥
        put("202403089", "2d09188f-bb8d-4b2b-a957-2f9f82e03ae2");// 3-1粉煤灰
        put("202403005", "d5cf69ef-032c-4f97-a52b-630426efd4a1");// 3-2粉煤灰
        put("202403033", "c1324c26-5406-4fd7-a554-8775640a9e68");// 3-3粉煤灰





    }
    };

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        paramMap.put("childId","408");
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","409");
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","447");
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","448");
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","444");
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","445");
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","450");
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","451");
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","461");
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","462");
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","467");
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","469");
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","471");
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","472");
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        // 柯诸一标2号站的料位欣盟对接 账号:TJ01，密码123456
        paramMap.put("childId","565");// 柯诸一标2号拌合站1号机组
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","566");// 柯诸一标2号拌合站2号机组
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        // 广西交投中铁一局的料位欣盟对接 账号:gxjt，密码123456
        paramMap.put("childId","522");// 1号机组
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","523");// 2号机组
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));

        // 杭州机场高铁的料位欣盟对接 账号:hzgt，密码123456
        paramMap.put("childId","639");// 1号机组
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","640");// 2号机组
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));
        paramMap.put("childId","641");// 3号机组
        doUpdateWeight(HttpUtil.post(dataurl, paramMap));


    }

    private void doUpdateWeight(String res) {
        JSONObject jsonObject = JSONUtil.parseObj(res);
        if (jsonObject.getInt("code") == 0 && jsonObject.getStr("msg").equals("操作成功")) {
            JSONArray jsarray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsarray.size(); i++) {
                JSONObject json = jsarray.getJSONObject(i);
                if (StrUtil.isNotBlank(licangMap.get(json.getStr("deveiceId")))) {
                    liaocangService.updateWeightBylcNo(licangMap.get(json.getStr("deveiceId")),json.getStr("currentCapacity"));
                }
            }
        }
    }
}
