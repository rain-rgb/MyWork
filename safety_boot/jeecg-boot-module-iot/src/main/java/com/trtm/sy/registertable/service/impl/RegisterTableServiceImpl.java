package com.trtm.sy.registertable.service.impl;

import com.trtm.sy.registertable.model.MergeCellModel;
import com.trtm.sy.registertable.model.RegisterTableRequest;
import com.trtm.sy.registertable.service.RegisterTableService;
import com.trtm.sy.registertable.mapper.FormRecordMapper;
import com.trtm.sy.registertable.model.SyFormRecord;
import org.apache.poi.xwpf.usermodel.*;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegisterTableServiceImpl implements RegisterTableService {


    // 编译正则表达式
    private Pattern PATTERNROW = Pattern.compile("row=\\d+");
    private Pattern PATTERNCOL = Pattern.compile("col=\\d+");
    private static final String COL = "col=";
    private static final String ROW = "row=";
    private static final String DH = "=";
    private static final String TABLE_LABEL = "table-label";
    private static final String TABLE_INPUT = "table-input";
    private static final String TABLE_DATA_RANGE = "table-date-range";
    private static final String TABLE_DATA = "table-date";

    @Resource
    private FormRecordMapper recordMapper;
//    @Resource
//    private SyYpjccsService syYpjccsService;
//    @Resource
//    private SyTableFormService syTableFormService;
//    @Resource
//    private SyJccsyzService syJccsyzService;
//    @Resource
//    private SyYpjccsBindYsjlMapper syYpjccsBindYsjlMapper;
//    @Autowired
//    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    @Override
    @Transactional
    public void handleFileUpload(MultipartFile file, RegisterTableRequest tableRequest, String tableNum) {
        if (file.isEmpty()) {
            throw new JeecgBootException("请上传文件");
        }

        recordMapper.delByTableNum(tableNum);

        try (InputStream fis = file.getInputStream();
             XWPFDocument document = new XWPFDocument(fis)) {

            List<XWPFTable> tables = document.getTables();

            // 遍历每个表格
            List<SyFormRecord> formRecords = new ArrayList<>();
            List<MergeCellModel> mergeCellModel = new ArrayList<>();
            for (XWPFTable table : tables) {
                // 遍历表格中的每一行
                List<XWPFTableRow> rows = table.getRows();

                for (int i = 0; i < rows.size(); i++) {
                    // 遍历行中的每一个单元格
                    List<XWPFTableCell> cells = rows.get(i).getTableCells();
                    int rolDemo = 0;
                    for (int j = 0; j < cells.size(); j++) {
                        boolean flag = false;
                        for (MergeCellModel cellModel : mergeCellModel) {
                            if (cellModel.getCurrentCos() == rolDemo) {
                                int currentRow = cellModel.getCurrentRow();
                                int cutoffLine = cellModel.getCutoffLine();
                                int num = currentRow + cutoffLine;
//                                System.err.println(num);
                                if (num > i) flag = true;
                            }
                        }
                        if (flag) {
                            rolDemo += getMergedCellText(table, i, j);
                            continue;
                        }
                        SyFormRecord formRecord = new SyFormRecord();
                        String content = cells.get(j).getText().trim();

                        // 使用正则表达式去除所有空白字符
                        content = content.replaceAll("\\s+", "");
                        if (content != null && content.contains("　")) {
                            content = content.replace("　", " "); // 替换全角空格为普通空格
                        }
                        content = content.trim();
                        formRecord.setColspan(getMergedCellText(table, i, j));
                        String col = "";
                        if (content.contains(COL)) {
                            Matcher matcher = PATTERNCOL.matcher(content);
                            if (matcher.find()) {
                                col = matcher.group();
                                String[] split = col.split(DH);
                                if (oConvertUtils.isEmpty(split[1])) {
                                    throw new JeecgBootException("带有col,请添加对应的行数,col=几列");
                                }
                                formRecord.setColspan(Integer.valueOf(split[1]));
                            }
                        }

                        String row = "";
                        int mergedRow = isMergedRow(table, i, j);
                        if (mergedRow > 1) {
                            formRecord.setRowspan(mergedRow);
                            MergeCellModel cellModel = new MergeCellModel();
                            cellModel.setCurrentRow(i);
                            cellModel.setCurrentCos(rolDemo);
                            cellModel.setCutoffLine(mergedRow);
                            mergeCellModel.add(cellModel);
                        }
                        rolDemo += formRecord.getColspan();
                        String value = content.replace(row, "").replace(col, "");
                        formRecord.setValue(value);
                        if (oConvertUtils.isNotEmpty(value)) {
                            formRecord.setInputType(TABLE_LABEL);
                            formRecord.setEmpty(0);
                        } else {
                            formRecord.setInputType(TABLE_INPUT);
                            formRecord.setEmpty(1);
                        }
                        formRecord.setKey("JG" + tableNum + "_" + i + "_" + j);
                        if (j > 0) {
                            keyChoose(formRecords, formRecord);
                        }
                        formRecord.setGroup(String.valueOf(i + 1));
                        formRecord.setReportNumber(tableNum);
                        formRecords.add(formRecord);

                    }
                }
            }
            formRecords.forEach(k->recordMapper.insert(k));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new JeecgBootException("文件异常,请重新上传或联系管理员!");
        }
    }

//    @Override
//    public void spiltFile(MultipartFile file, RegisterTableRequest tableRequest) {
//        try {
//            InputStream inputStream = file.getInputStream();
//            XWPFDocument document = new XWPFDocument(inputStream);
//
//            List<XWPFParagraph> paragraphs = document.getParagraphs();
//            for (XWPFParagraph paragraph : paragraphs) {
//                replaceSpecialSpaces(paragraph);
//            }
//            for (XWPFParagraph para : paragraphs) {
//                // 处理每个段落的内容
//                List<XWPFRun> runs = para.getRuns();
//                for (XWPFRun run : runs) {
//                    String text = run.getText(0); // 获取每个run的文本内容
////                    System.err.println("Paragraph Text: " + text);
//                }
//            }
//            List<String> list = new ArrayList<>();
//
//            for (int i = 879; i <= 879; i++) {
//                //读取表名和表号
//                XWPFParagraph paragraph1 = paragraphs.get(i);
//                String tableName = paragraph1.getText().trim();
//                System.out.println(tableName);
//                if (tableName.contains("记录表") || tableName.contains("报告")) {
//                    list.add(tableName);
//                }
//            }
//
//
//            List<XWPFTable> tables = document.getTables();
//
//            int count = 0;
//            for (int i = 808; i <= 808; i++) {
//                XWPFTable table = tables.get(i);
//                List<XWPFTableRow> rows = table.getRows();
//                saveForm(table, rows, list.get(i - 808));
//                count++;
//            }
//
//            System.out.println("count ===" + count);
//            System.out.println("list ===" + list.size());
//            System.out.println();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    @Transactional
//    public void updateTableFormPattern(String tableNum, List<RegisterTableRequest> requestList) {
//        List<SyFormRecord> currentList = recordService.list(Wrappers.lambdaQuery(new SyFormRecord()).eq(SyFormRecord::getReportNumber, tableNum));
//        List<MergeCellModel> newRecordList = new ArrayList<>();
//        List<SyFormRecord> delRecordList = new ArrayList<>();
//        List<List<SyFormRecord>> groupList = new ArrayList<>();
//        try {
//            List<SyFormRecord> currentGroupList = new ArrayList<>();
//            int group = 1;
//            for (int i = 0; i < currentList.size(); i++) {
//                if (Integer.parseInt(currentList.get(i).getGroup()) == group) {
//                    currentGroupList.add(currentList.get(i));
//                } else {
//                    groupList.add(currentGroupList);
//                    group = Integer.parseInt(currentList.get(i).getGroup());
//                    currentGroupList = new ArrayList<>();
//                    currentGroupList.add(currentList.get(i));
//                }
//                int count = 0;
//                for (int j = 0; j < requestList.size(); j++) {
//                    if (currentList.get(i).getId() == requestList.get(j).getId()) {
//                        if (currentList.get(i).getRowspan() != Integer.valueOf(requestList.get(j).getRow())) {
//                            MergeCellModel cellModel = new MergeCellModel();
//                            cellModel.setId(currentList.get(i).getId());
//                            cellModel.setCurrentRow(currentList.get(i).getRowspan());
//                            cellModel.setCutoffLine(Integer.parseInt(requestList.get(j).getRow()));
//                            cellModel.setGroup(Integer.valueOf(currentList.get(i).getGroup()));
//                            if (currentList.get(i).getRowspan() < Integer.parseInt(requestList.get(j).getRow())) {
//                                cellModel.setStatus(true); //true表示当前合并行是增加格子的合并数
//                            } else if (currentList.get(i).getRowspan() > Integer.parseInt(requestList.get(j).getRow())) {
//                                cellModel.setStatus(false); //false表示当前合并行是减少格子的数量
//                            }
//                            newRecordList.add(cellModel);
//                        }
//                    }
//                }
//            }
//            for (int i = 0; i < newRecordList.size(); i++) {
//                if (newRecordList.get(i).getStatus()) {
//                    if (newRecordList.get(i).getCurrentRow() == 1) {
//                        int updateGroup = newRecordList.get(i).getGroup();
//                        int targetIndex = updateGroup + (newRecordList.get(i).getCurrentRow()) - 1;
//                        int newRecordIndex = -1;
//                        for (int k = updateGroup - 1; k < targetIndex; k++) {
//                            List<SyFormRecord> list = groupList.get(k);
//                            if (newRecordIndex == -1) {
//                                for (int d = 0; d < list.size(); d++) {
//                                    if (newRecordList.get(i).getId() == list.get(d).getId()) {
//                                        newRecordIndex = d;
//                                    }
//                                }
//                            }
//                            if (newRecordIndex != -1 && k > updateGroup - 1) {
//                                delRecordList.add(list.get(newRecordIndex));
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new JeecgBootException("更改错误, 请重新调整");
//        }
//    }
//
//    @Override
//    @Transactional
//    public void saveFormList(List<List<BaseForm>> request, String tableNum) {
//        List<SyFormRecord> syFormRecordLists = new ArrayList<>();
//        AtomicInteger group = new AtomicInteger(1);
//        request.forEach(firstList -> {
//            firstList.forEach(n -> {
//                SyFormRecord syFormRecord = new SyFormRecord();
//                syFormRecord.setId(n.getId());
//                syFormRecord.setInputType(n.getType());
//                syFormRecord.setKey(n.getKey());
//                syFormRecord.setStyle(n.getStyle());
//                syFormRecord.setValue(n.getValue());
//                syFormRecord.setColspan(n.getColspan());
//                syFormRecord.setRowspan(n.getRowspan());
//                syFormRecord.setGroup(String.valueOf(group.get()));
//                syFormRecord.setReportNumber(tableNum);
//                syFormRecord.setDictType(n.getDictType());
//                syFormRecord.setRequired(n.getRequired());
//                syFormRecord.setLength(n.getLength());
//                syFormRecord.setAbeam(n.getAbeam());
//                syFormRecord.setEmpty(n.getEmpty());
//                syFormRecord.setBgkey(n.getBgkey());
////                syFormRecord.setBgbm(n.getBgbm());
//                syFormRecordLists.add(syFormRecord);
////                // 构建查询
////                BoolQueryBuilder base_query = QueryBuilders.boolQuery();
////                base_query.must(QueryBuilders.termQuery("jlbbm", tableNum));
////                base_query.must(QueryBuilders.termQuery("bgbm", n.getBgbm()));
//////                base_query.must(QueryBuilders.termQuery("bgkey", n.getBgkey()));
////                base_query.must(QueryBuilders.termQuery("jlbkey", n.getKey()));
////                // 执行查询并获取结果
////                NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
////                        .withQuery(base_query)
////                        .build();
////                SearchHits<BGKeyMapping> searchHits = elasticsearchRestTemplate.search(searchQuery, BGKeyMapping.class);
////                List<BGKeyMapping> content = StreamSupport.stream(searchHits.spliterator(), false)
////                        .map(searchHit -> {
////                            return searchHit.getContent();
////                        })
////                        .collect(Collectors.toList());
////                content.forEach(hit->{
////                    String documentId = hit.getId();
////                    elasticsearchRestTemplate.delete(documentId,BGKeyMapping.class);
////                });
//            });
//            group.getAndIncrement();
//        });
//        recordService.updateBatchById(syFormRecordLists);
//
//    }
//
//    @Override
//    public void uploadWord(MultipartFile file) {
//        if (file.isEmpty()) {
//            throw new JeecgBootException("请上传文件");
//        }
//        try (InputStream fis = file.getInputStream();
//             XWPFDocument document = new XWPFDocument(fis)) {
//            List<String> tableNamelist = new ArrayList<>();
//            List<String> tableBHlist = new ArrayList<>();
//            String BHString = "";
//            for (int i = 0; i < document.getParagraphs().size(); i++) {
//                String tableName = document.getParagraphs().get(i).getText().trim();
//                System.out.println(tableName);
//                if (tableName.contains("标定表") || tableName.contains("记录表") || tableName.contains("检测报告")|| tableName.contains("通用表")|| tableName.contains("检验报告")) {
//                    tableNamelist.add(tableName);
//                }
//                if (tableName.contains("JG") || tableName.contains("BG")) {
//                    String lastName = document.getParagraphs().get(i - 1).getText().trim();
//                    if (lastName.contains("JG") || lastName.contains("BG")) {
////                        String nextName = document.getParagraphs().get(i+1).getText().trim();
////                        if (!nextName.contains("JG")||!nextName.contains("BG")) {
////                            tableBHlist.remove(lastName);
////                            tableBHlist.add(lastName +","+tableName);
////                        }else{
//                        String temp = tableBHlist.get(tableBHlist.size() - 1);
//                        tableBHlist.remove(tableBHlist.size() - 1);
//                        tableBHlist.add(temp + "," + tableName);
////                        }
//                    } else {
//                        tableBHlist.add(tableName);
//                    }
//                }
//            }
//
//            List<XWPFTable> tables = document.getTables();
//            for (int temp = 0; temp < tables.size(); temp++) {
//                XWPFTable table = tables.get(temp);
//                String tablename = tableNamelist.get(temp);
//                String tableBH = tableBHlist.get(temp);
//                SyTableForm syTableForm = new SyTableForm();
//                syTableForm.setJlbmc(tablename);
//
//                String[] split1 = tableBH.trim().split("[，,、]");
//                List<String> strings = Arrays.asList(split1);
//
//                String BH = strings.get(0).trim();
//                List<SyTableForm> list1 = syTableFormService.list(Wrappers.lambdaQuery(new SyTableForm()).like(SyTableForm::getJlbbm, BH));
//                if (oConvertUtils.isNotEmpty(list1)) {
//                    int i = list1.size() + 1;
//                    BH = BH + "-" + i;
//                    syTableForm.setJlbbm(BH);
//                } else {
//                    syTableForm.setJlbbm(BH);
//                }
//                if (tablename.contains("记录表") || tablename.contains("标定表")) {
//                    syTableForm.setType(0);
//                } else if (tablename.contains("检测报告")) {
//                    syTableForm.setType(1);
//                }
//                syTableForm.setDelFlag("N");
//                syTableForm.setBz(tableBH);
//                syTableFormService.addSyTableForm(syTableForm);
//
//
//                strings.forEach(any -> {
//                    if (any.startsWith("JG")) {
//                        String numbers = any.substring("JG".length());
//                        char lastChar = any.charAt(any.length() - 1);
//                        if (lastChar >= 'a' && lastChar <= 'z') {
//                            numbers = numbers.substring(0, numbers.length() - 1);
//                        }
//                        QueryWrapper<SyYpjccs> queryWrapper = new QueryWrapper<>();
//                        queryWrapper.like("yp_jccs_code", numbers);
//                        List<SyYpjccs> list = syYpjccsService.list(queryWrapper);
//                        if (oConvertUtils.isNotEmpty(list)) {
//                            list.forEach(n -> {
//                                SyYpjccsBindYsjl ysjl = new SyYpjccsBindYsjl();
//                                ysjl.setYpjccsId(n.getYpjccsId());
//                                ysjl.setYpjccsName(n.getYpJccs());
//                                ysjl.setJlbid(String.valueOf(syTableForm.getId()));
//                                syYpjccsBindYsjlMapper.insert(ysjl);
//                            });
//
//                        }
//                    }
//                });
//
//                // 遍历每个表格
//                List<SyFormRecord> formRecords = new ArrayList<>();
//                List<MergeCellModel> mergeCellModel = new ArrayList<>();
//                // 遍历表格中的每一行
//                List<XWPFTableRow> rows = table.getRows();
//
//                for (int i = 0; i < rows.size(); i++) {
//                    // 遍历行中的每一个单元格
//                    List<XWPFTableCell> cells = rows.get(i).getTableCells();
//                    int rolDemo = 0;
//                    for (int j = 0; j < cells.size(); j++) {
//                        boolean flag = false;
//                        for (MergeCellModel cellModel : mergeCellModel) {
//                            if (cellModel.getCurrentCos() == rolDemo) {
//                                int currentRow = cellModel.getCurrentRow();
//                                int cutoffLine = cellModel.getCutoffLine();
//                                int num = currentRow + cutoffLine;
////                                System.err.println(num);
//                                if (num > i) flag = true;
//                            }
//                        }
//                        if (flag) {
//                            rolDemo += getMergedCellText(table, i, j);
//                            continue;
//                        }
//                        SyFormRecord formRecord = new SyFormRecord();
//                        String content = cells.get(j).getText().trim();
//
//                        // 使用正则表达式去除所有空白字符
//                        content = content.replaceAll("\\s+", "");
//                        if (content != null && content.contains("　")) {
//                            content = content.replace("　", " "); // 替换全角空格为普通空格
//                        }
//                        content = content.trim();
//                        formRecord.setColspan(getMergedCellText(table, i, j));
//                        String col = "";
//
//                        String row = "";
//                        int mergedRow = isMergedRow(table, i, j);
//                        if (mergedRow > 1) {
//                            formRecord.setRowspan(mergedRow);
//                            MergeCellModel cellModel = new MergeCellModel();
//                            cellModel.setCurrentRow(i);
//                            cellModel.setCurrentCos(rolDemo);
//                            cellModel.setCutoffLine(mergedRow);
//                            mergeCellModel.add(cellModel);
//                        }
//                        rolDemo += formRecord.getColspan();
//                        String value = content.replace(row, "").replace(col, "");
//                        formRecord.setValue(value);
//                        if (oConvertUtils.isNotEmpty(value)) {
//                            formRecord.setInputType(TABLE_LABEL);
//                            formRecord.setEmpty(0);
//                        } else {
//                            formRecord.setInputType(TABLE_INPUT);
//                            formRecord.setEmpty(1);
//                        }
//                        formRecord.setKey("JG" + temp + "_" + i + "_" + j);
//                        if (j > 0) {
//                            keyChoose(formRecords, formRecord);
//                        }
//                        formRecord.setGroup(String.valueOf(i + 1));
//                        formRecord.setReportNumber(BH);
//                        formRecords.add(formRecord);
//
//                    }
//                }
//                recordService.saveBatch(formRecords);
//            }
//
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            throw new JeecgBootException("文件异常,请重新上传或联系管理员!");
//        }
//
//    }
//
    private void keyChoose(List<SyFormRecord> formRecords, SyFormRecord formRecord) {
        SyFormRecord syFormRecord = formRecords.get(formRecords.size() - 1);
        String upValue = syFormRecord.getValue();
        switch (upValue) {
            case "委托单位":
                formRecord.setKey("wtdw");
                formRecord.setEmpty(0);
                break;
            case "样品状态":
                formRecord.setKey("ypzt");
                formRecord.setEmpty(0);
                break;
            case "委托编号":
                formRecord.setKey("wtdbh");
                formRecord.setEmpty(0);
                break;
            case "工程名称":
                formRecord.setKey("gcmc");
                formRecord.setEmpty(0);
                break;
            case "样品名称":
                formRecord.setKey("ypmc");
                formRecord.setEmpty(0);
                break;
            case "工程部位/用途":
                formRecord.setKey("gcbw");
                formRecord.setEmpty(0);
                break;
            case "样品数量":
                formRecord.setKey("ypsl");
                formRecord.setEmpty(0);
                break;
            case "来样日期/委托日期":
                formRecord.setKey("wtrq");
                formRecord.setEmpty(0);
                break;
            case "样品编号":
                formRecord.setKey("ypbh");
                formRecord.setEmpty(0);
                break;
            case "送样人/委托人":
                formRecord.setKey("syr");
                formRecord.setEmpty(0);
                break;
            case "检测类别":
                formRecord.setKey("jclb");
                formRecord.setEmpty(0);
                break;
            case "见证人及编号/见证单位":
                formRecord.setKey("jzr");
                formRecord.setEmpty(0);
                break;
            case "检测依据":
                formRecord.setKey("jcyj");
                formRecord.setEmpty(0);
                break;
            case "判定依据":
                formRecord.setKey("pdyj");
                formRecord.setEmpty(0);
                break;
            case "样品规格/等级":
            case "类型规格":
                formRecord.setKey("ypggdj");
                formRecord.setEmpty(0);
                break;
            case "试验条件":
                formRecord.setKey("sytj");
                formRecord.setEmpty(0);
                break;
            case "生产单位":
            case "生产厂家":
                formRecord.setKey("scdw");
                formRecord.setEmpty(0);
                break;
            case "出厂批号":
            case "生产批号":
                formRecord.setKey("ccph");
                formRecord.setEmpty(0);
                break;
            case "代表数量":
                formRecord.setKey("dbsl");
                formRecord.setEmpty(0);
                break;
            case "样品信息":
                formRecord.setKey("ypxx");
                formRecord.setEmpty(0);
                break;
            default:
                break;
        }
    }


//    private void saveForm(XWPFTable table, List<XWPFTableRow> rows, String tableNum) {
//        List<SyFormRecord> formRecords = new ArrayList<>();
//        List<MergeCellModel> mergeCellModel = new ArrayList<>();
//        for (int i = 0; i < rows.size(); i++) {
//            // 遍历行中的每一个单元格
//            List<XWPFTableCell> cells = rows.get(i).getTableCells();
//
//            for (int j = 0; j < cells.size(); j++) {
//                boolean flag = false;
//                for (MergeCellModel cellModel : mergeCellModel) {
//                    if (cellModel.getCurrentCos() == j) {
//                        int currentRow = cellModel.getCurrentRow();
//                        int cutoffLine = cellModel.getCutoffLine();
//                        int num = currentRow + cutoffLine;
////                        System.err.println(num);
//                        if (num > i) flag = true;
//                    }
//                }
//                if (flag) continue;
//                SyFormRecord formRecord = new SyFormRecord();
//                String content = cells.get(j).getText().trim();
//
//                // 使用正则表达式去除所有空白字符
//                content = content.replaceAll("\\s+", "");
//                if (content != null && content.contains("　")) {
//                    content = content.replace("　", " "); // 替换全角空格为普通空格
//                }
//                formRecord.setColspan(getMergedCellText(table, i, j));
//                String col = "";
//                if (content.contains(COL)) {
//                    Matcher matcher = PATTERNCOL.matcher(content);
//                    if (matcher.find()) {
//                        col = matcher.group();
//                        String[] split = col.split(DH);
//                        if (oConvertUtils.isEmpty(split[1])) {
//                            throw new JeecgBootException("带有col,请添加对应的行数,col=几列");
//                        }
//                        formRecord.setColspan(Integer.valueOf(split[1]));
//                    }
//                }
//
//                String row = "";
//                if (isFirstCellInMergedRegion(cells.get(j))) {
//                    if (content.contains(ROW)) {
//                        Matcher matcher = PATTERNROW.matcher(content);
//                        if (matcher.find()) {
//                            row = matcher.group();
//                            String[] split = row.split(DH);
//                            if (oConvertUtils.isEmpty(split[1])) {
//                                throw new JeecgBootException("带有row,请添加对应的行数,row=几行");
//                            }
//                            formRecord.setRowspan(Integer.valueOf(split[1]));
//
//                            MergeCellModel cellModel = new MergeCellModel();
//                            cellModel.setCurrentRow(i);
//                            cellModel.setCurrentCos(j);
//                            cellModel.setCutoffLine(Integer.valueOf(split[1]));
//                            mergeCellModel.add(cellModel);
//                        }
//                    }
//                }
//
//                String value = content.replace(row, "").replace(col, "");
//                formRecord.setValue(value);
//                if (oConvertUtils.isNotEmpty(value)) {
//                    formRecord.setInputType(TABLE_LABEL);
//                } else {
//                    formRecord.setInputType(TABLE_INPUT);
//                }
//                formRecord.setGroup(String.valueOf(i + 1));
//                formRecord.setReportNumber(tableNum);
//                formRecords.add(formRecord);
//
//            }
//
//        }
//        recordService.saveBatch(formRecords);
//
//    }
//
//    // 替换段落中的全角空格为普通空格
//    private void replaceSpecialSpaces(XWPFParagraph paragraph) {
//        for (XWPFRun run : paragraph.getRuns()) {
//            String text = run.getText(0);
//            if (text != null && text.contains("　")) {
//                text = text.replace("　", " "); // 替换全角空格为普通空格
//                run.setText(text, 0);
//            }
//        }
//    }
//
//    // 替换单元格中的全角空格为普通空格
//    private void replaceSpecialSpaces(XWPFTableCell cell) {
//        for (XWPFParagraph para : cell.getParagraphs()) {
//            replaceSpecialSpaces(para);
//        }
//    }
//
//
//    // 判断单元格是否为合并区域的起始单元格（即左上角）
//    private static boolean isFirstCellInMergedRegion(XWPFTableCell cell) {
//        // 检查单元格是否包含合并信息
//        if (cell.getCTTc() != null && cell.getCTTc().isSetTcPr() && cell.getCTTc().getTcPr().isSetVMerge()) {
//            // 获取VMerge对象
//            CTVMerge vMerge = cell.getCTTc().getTcPr().getVMerge();
//            // 检查是否为合并单元格的起始单元格
//            if (vMerge.getVal() != null && vMerge.getVal().toString().equals("restart")) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    // 获取合并区域的结束单元格
//    public static XWPFTableCell getEndCellOfMergedRegion(XWPFTable table, int startRowIndex, int colIndex) {
//        XWPFTableRow startRow = table.getRow(startRowIndex);
//        XWPFTableCell startCell = startRow.getCell(colIndex);
//
//        // 遍历起始单元格所在行以下的所有行，找到合并区域的最后一个单元格
//        for (int rowIndex = startRowIndex; rowIndex < table.getNumberOfRows(); rowIndex++) {
//            XWPFTableRow row = table.getRow(rowIndex);
//            XWPFTableCell cell = row.getCell(colIndex);
//
//            if (null != cell && isFirstCellInMergedRegion(cell)) {
//                System.out.println("结束得单元格   列为 ：" + colIndex + " 行为：" + rowIndex);
//                return cell; // 找到结束单元格
//
//            }
//        }
//
//        System.out.println("没有找到对应的结束单元格");
//        return null; // 没有找到对应的结束单元格
//    }
//

    // 获取合并单元格的内容
    private int getMergedCellText(XWPFTable table, int rowIndex, int colIndex) {
        XWPFTableCell cell = table.getRow(rowIndex).getCell(colIndex);

        // 检查单元格是否为合并单元格
        if (cell.getCTTc().getTcPr() != null && cell.getCTTc().getTcPr().isSetGridSpan()) {
            // 获取合并的列数和行数
            int colSpan = cell.getCTTc().getTcPr().getGridSpan().getVal().intValue();
//            System.err.println("colSpan=====" + colSpan);

//            int rowSpan = 1; // 合并的行数，默认为1行
//
//            // 查找合并的行数（由于POI不直接提供，通常是需要自己判断）
//            for (int i = rowIndex + 1; i < rowIndex + colSpan; i++) {
//                XWPFTableCell nextRowCell = table.getRow(i).getCell(colIndex);
//                if (nextRowCell != null && nextRowCell.getParagraphs().size() > 0) {
//                    rowSpan++;
//                } else {
//                    break;
//                }
//            }
//
//            System.err.println("rowSpan===== ==== " + rowSpan);
//            // 构建合并区域的内容
//            StringBuilder cellText = new StringBuilder();
//            for (int i = 0; i < rowSpan; i++) {
//                XWPFTableCell mergedCell = table.getRow(rowIndex + i).getCell(colIndex);
//                for (XWPFParagraph paragraph : mergedCell.getParagraphs()) {
//                    cellText.append(paragraph.getText()).append("\t"); // 换行符或其他内容
//                }
//            }
//            System.err.println(cellText.toString().trim() + "合并行数：" + rowSpan);
            return colSpan;
        } else {
            // 非合并单元格，直接返回内容
            return 1;
        }
    }  // 获取合并单元格的内容

    // 获取行合并单元格的行数
    private int isMergedRow(XWPFTable table, int rowIndex, int colIndex) {
        int rowSpan = 1;
        XWPFTableCell cell = table.getRow(rowIndex).getCell(colIndex);
        CTVMerge vMerge = cell.getCTTc().getTcPr().getVMerge();
        if(oConvertUtils.isNotEmpty(vMerge)){
            if (vMerge.getVal() == STMerge.RESTART) {
                for (int i = rowIndex+1; i < table.getRows().size()-1; i++) {
                    XWPFTableCell nextcell = table.getRow(i).getCell(colIndex);
                    if(oConvertUtils.isEmpty(nextcell)){
                        break;
                    }
                    CTVMerge nextvMerge = nextcell.getCTTc().getTcPr().getVMerge();
                    if(oConvertUtils.isNotEmpty(nextvMerge)&&nextvMerge.getVal() == STMerge.CONTINUE){
                        rowSpan++;
                    }else{
                        break;
                    }
                }
            }
        }
        return rowSpan;
    }
//
//
////    public static void main(String[] args) throws IOException {
////        FileInputStream fileInputStream = new FileInputStream("D:\\work\\testFile\\cjl1.xlsx");
////        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
////        XSSFSheet sheet = workbook.getSheetAt(0);
////
////        for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator.hasNext(); ) {
////            Row row = rowIterator.next();
////            for (Iterator<Cell> cellIterator = row.cellIterator(); cellIterator.hasNext(); ) {
////                Cell cell = cellIterator.next();
////
////                // 检查是否为合并单元格的一部分
////                if (cell.isIsMerged()) {
////                    System.out.println("Cell " + cell.getReference() + " is part of a merged region.");
////
////                    // 获取合并区域的索引
////                    int mergedRegionIndex = cell.getMergedRegion();
////                    CellRangeAddress mergedRegion = sheet.getMergedRegion(mergedRegionIndex);
////
////                    // 打印合并区域的信息
////                    System.out.println("Merged region: " + mergedRegion.formatAsString());
////                }
////            }
////        }
////
////        workbook.close();
////        fileInputStream.close();
////    }
//
//
////    public static void main(String[] args) throws Exception{
////        // 假设代码，用于说明
////        XWPFDocument doc = new XWPFDocument(new FileInputStream("D:\\work\\testFile\\检测数据报告编制导则(24.1.11).docx"));
////
////// 假设你已经有了一个方法来模拟分页
////        List<List<XWPFParagraph>> pages = simulatePages(doc);
////
////        for (int i = 0; i < pages.size(); i++) {
////            XWPFDocument newDoc = new XWPFDocument();
////            // 复制段落到新文档
////            for (XWPFParagraph p : pages.get(i)) {
////
////                // 复制段落逻辑（这里需要具体实现）
////                // newDoc.addParagraph(p); // 注意：Apache POI不直接支持复制段落
////            }
////            // 保存新文档
////            FileOutputStream out = new FileOutputStream("D:\\work\\testFile\\output_" + (i + 1) + ".docx");
////            newDoc.write(out);
////            out.close();
////            newDoc.close();
////        }
////
////        doc.close();
////    }
////    // 假设每页包含 N 个段落（或根据需要进行调整）
////    private static final int PARAGRAPHS_PER_PAGE = 10;
////    public static List<List<XWPFParagraph>> simulatePages(XWPFDocument doc) {
////        List<XWPFParagraph> paragraphs = doc.getParagraphs();
////        List<List<XWPFParagraph>> pages = new ArrayList<>();
////
////        int currentPageIndex = 0;
////        List<XWPFParagraph> currentPage = new ArrayList<>();
////
////        for (XWPFParagraph paragraph : paragraphs) {
////            // 假设每个段落都足够小，可以放入一页中
////            // 或者你可以根据字符数、段落高度等更复杂的逻辑来判断
////            currentPage.add(paragraph);
////
////            if (currentPage.size() >= PARAGRAPHS_PER_PAGE) {
////                // 当一页满时，将其添加到页面中列表，并开始新的一页
////                pages.add(currentPage);
////                currentPage = new ArrayList<>();
////                currentPageIndex++;
////            }
////        }
////
////        // 添加最后一页（如果它不为空）
////        if (!currentPage.isEmpty()) {
////            pages.add(currentPage);
////        }
////
////        return pages;
////    }


}
