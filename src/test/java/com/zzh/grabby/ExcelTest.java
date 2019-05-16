package com.zzh.grabby;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.zzh.grabby.common.util.Jsr303Util;
import com.zzh.grabby.entity.ExcelVerifyEntity;
import com.zzh.grabby.entity.UserExcel;
import com.zzh.grabby.entity.ViliGroupOne;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zzh
 * @date 2019/2/12
 */
@Slf4j
public class ExcelTest  {

    /**
     * 导入测试
     * @throws Exception
     */
    @Test
    public void testImport() throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setStartSheetIndex(1);
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("import/aa.xlsx");

        List<UserExcel> list = ExcelImportUtil.importExcel(
                in,
                UserExcel.class, params);

        for (int i = 0; i < list.size(); i++) {
            String s = Jsr303Util.check(list.get(i));
            System.out.println("导入失败，第"+(i+1)+"行");
            System.out.println(s);
        }
        System.out.println(list.size());
        System.out.println(list);

    }


    /**
     * 导出测试
     * @throws IOException
     */
    @Test
    public void testExport() throws IOException {
        List<UserExcel> list = new ArrayList<UserExcel>();
        UserExcel userExcel = new UserExcel();
        userExcel.setAddress("Aaa");
        userExcel.setDepartmentName("Aaa");
        userExcel.setEmail("Aaa");
        userExcel.setMobile("11111111");
        list.add(userExcel);

        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);
//        ExportParams params = new ExportParams("总部人员清单", "测试", );
        Workbook workbook = ExcelExportUtil.exportExcel(params, UserExcel.class, list);
        File savefile = new File("D:/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/excel/oneHundredThousandRowTest.xlsx");
        workbook.write(fos);
        fos.close();
    }

    @Test
    public void importValidate() {
        try {
            ImportParams params = new ImportParams();
            params.setNeedVerfiy(true);
            params.setVerfiyGroup(new Class[]{ViliGroupOne.class});
//            ExcelImportResult<ExcelVerifyEntity> result = ExcelImportUtil.importExcelMore(
//                    new File(FileUtilTest.getWebRootPath("import/verfiy.xlsx")),
//                    ExcelVerifyEntity.class, params);
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("import/verfiy.xlsx");
            ExcelImportResult<ExcelVerifyEntity> result = ExcelImportUtil.importExcelMore(
                    in,
                    ExcelVerifyEntity.class, params);

            FileOutputStream fos = new FileOutputStream("D:/excel/ExcelVerifyTest.basetestonlyFail_success.xlsx");
            result.getWorkbook().write(fos);
            fos.close();
            for (int i = 0; i < result.getList().size(); i++) {
                System.out.println(ReflectionToStringBuilder.toString(result.getList().get(i)));
            }
            System.out.println("---------------------- fail -------------------");
            fos = new FileOutputStream("D:/excel/ExcelVerifyTest.basetestonlyFail.xlsx");
            result.getFailWorkbook().write(fos);
            fos.close();

            //失败的数据
            for (int i = 0; i < result.getFailList().size(); i++) {
                ExcelVerifyEntity x = result.getFailList().get(i);
                System.out.println(ReflectionToStringBuilder.toString(result.getFailList().get(i)));
            }
            Assert.assertTrue(result.getList().size() == 2);
            Assert.assertTrue(result.isVerfiyFail());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }


}
