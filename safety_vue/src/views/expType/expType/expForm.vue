<template>
  <div>
<!--    <a-form-model layout="inline" :width="modal.width">-->
<!--      <a-form-model-item label="施工/委托单位">-->
<!--        <a-input placeholder="施工单位名称"/>-->
<!--      </a-form-model-item>-->
<!--      <a-form-model-item label="工程名称">-->
<!--        <a-input/>-->
<!--      </a-form-model-item>-->
<!--      <a-form-model-item label="工程部位/用途">-->
<!--        <a-input/>-->
<!--      </a-form-model-item>-->
<!--      <a-form-model-item label="委托/任务编号">-->
<!--        <a-input/>-->
<!--      </a-form-model-item>-->
<!--      <a-form-model-item label="试验日期">-->
<!--        <a-range-picker @change="dateChange"/>-->
<!--      </a-form-model-item>-->
<!--      <a-form-model-item label="样品信息">-->
<!--        <a-input/>-->
<!--      </a-form-model-item>-->
<!--      <a-form-model-item label="检测依据">-->
<!--        <a-input/>-->
<!--      </a-form-model-item>-->
<!--      <a-form-model-item label="判定依据">-->
<!--        <a-input/>-->
<!--      </a-form-model-item>-->
<!--      <a-form-model-item label="主要仪器设备名称及编号">-->
<!--        <a-input/>-->
<!--      </a-form-model-item>-->
<!--      <a-form-model-item label="产地">-->
<!--        <a-input/>-->
<!--      </a-form-model-item>-->
<!--      <a-form-model-item label="类型规格(mm)">-->
<!--        <a-input/>-->
<!--      </a-form-model-item>-->
<!--      <a-form-model-item label="代表数量">-->
<!--        <a-input/>-->
<!--      </a-form-model-item>-->
<!--      <a-form-model-item label="取样地点">-->
<!--        <a-input/>-->
<!--      </a-form-model-item>-->
<!--      <a-form-model-item label="取样日期">-->
<!--        <a-input/>-->
<!--      </a-form-model-item>-->
<!--      <a-form-model-item label="选择标准">-->
<!--        <a-input/>-->
<!--      </a-form-model-item>-->
<!--      <br/>-->
<!--      <h3>以下数据输入后由算法自动填充</h3>-->
<!--    </a-form-model>-->
<!--    <a-form-model-item>-->
<!--&lt;!&ndash;      <a-table :bordered="table.bordered">&ndash;&gt;-->
<!--&lt;!&ndash;        &ndash;&gt;-->
<!--&lt;!&ndash;      </a-table>&ndash;&gt;-->
<!--      <table :bordered="table.bordered">-->
<!--        <tr>-->
<!--          <td width="20%">序号</td>-->
<!--          <td width="20%">检测项目</td>-->
<!--          <td width="20%">技术指标</td>-->
<!--          <td colspan="3" width="20%">检测结果</td>-->
<!--          <td width="20%">结果判定</td>-->
<!--        </tr>-->
<!--        <tr>-->
<!--          <td width="20%">1</td>-->
<!--          <td width="20%">含泥量或0.075mm以下颗粒含量(%)</td>-->
<!--          <td width="20%"><a-input/></td>-->
<!--          <td colspan="3" width="20%">检测结果</td>-->
<!--          <td width="20%"><a-input/></td>-->
<!--        </tr>-->
<!--      </table>-->
<!--    </a-form-model-item>-->





    <div class="tableContent" style="height: 640px;">
      <table class="layui-table"
             style="border: 2px solid #000;margin-bottom:-1px;border-bottom:none; box-sizing: border-box;">
        <tr>
          <td colspan="2" width="20%">产地</td>
          <td colspan="13"><input type="text" name="chandi" id="chandi" /></td>
        </tr>
        <tr>
          <td colspan="2">类型规格(mm)</td>
          <td colspan="4"><input type="text" name="lxgg" id="lxgg" /></td>
          <td colspan="3">代表数量(m³)</td>
          <td colspan="6"><input type="text" name="dbsl" id="dbsl" /></td>
        </tr>
        <tr>
          <td width="6%">序号</td>
          <td colspan="5">检测项目</td>
          <td colspan="3">
            技术指标
            <select name="jszbtype" id="jszbtype" class="jszb">
              <option value="Ⅰ类">Ⅰ类</option>
              <option value="Ⅱ类">Ⅱ类</option>
              <option value="Ⅲ类">Ⅲ类</option>
            </select>
          </td>
          <td colspan="3">检测结果</td>
          <td colspan="3">结果判定</td>
        </tr>
        <tr>
          <td>1</td>
          <td colspan="5">含泥量(小于0.075mm含量)(%)</td>
          <td colspan="3"><input type="text" name="hnlzb" id="hnlzb" /></td>
          <td colspan="3"><input type="text" name="hnlpjz" id="hnlpjz" readonly="readonly" /></td>
          <td colspan="3"><input type="text" name="hnljg" id="hnljg" /></td>
        </tr>
        <tr>
          <td>2</td>
          <td colspan="5">泥块含量(%)</td>
          <td colspan="3"><input type="text" name="nkzb" id="nkzb" /></td>
          <td colspan="3"><input type="text" name="nkhlpjz" id="nkhlpjz" readonly="readonly" /></td>
          <td colspan="3"><input type="text" name="nkjg" id="nkjg" /></td>
        </tr>
        <tr>
          <td>3</td>
          <td colspan="5">针,片状颗粒含量(%)</td>
          <td colspan="3"><input type="text" name="klhlzb" id="klhlzb" /></td>
          <td colspan="3"><input type="text" name="zpzzhl" id="zpzzhl" readonly="readonly" /></td>
          <td colspan="3"><input type="text" name="klhljg" id="klhljg" /></td>
        </tr>
        <tr>
          <td>4</td>
          <td colspan="5">压碎值(%)</td>
          <td colspan="3"><input type="text" name="yszzb" id="yszzb" /></td>
          <td colspan="3"><input type="text" name="hshjlysz" id="hshjlysz" readonly="readonly" /></td>
          <td colspan="3"><input type="text" name="yszjg" id="yszjg" /></td>
        </tr>
        <tr>
          <td>5</td>
          <td colspan="5">含水率(%)</td>
          <td colspan="3"><input type="text" name="hslzb" id="hslzb" /></td>
          <td colspan="3"><input type="text" name="pjz" id="pjz" readonly="readonly" /></td>
          <td colspan="3"><input type="text" name="hsljg" id="hsljg" /></td>
        </tr>
        <tr>
          <td>6</td>
          <td colspan="5">坚固性指标(%)</td>
          <td colspan="3"><input type="text" name="jgxzb" id="jgxzb" /></td>
          <td colspan="3"><input type="text" name="zjpjssl" id="zjpjssl" readonly="readonly" /></td>
          <td colspan="3"><input type="text" name="jgxjg" id="jgxjg" /></td>
        </tr>
        <tr>
          <td>7</td>
          <td colspan="5">磨耗损失(%)</td>
          <td colspan="3"><input type="text" name="mhsszb" id="mhsszb" /></td>
          <td colspan="3"><input type="text" name="mhcdz" id="mhcdz" readonly="readonly" /></td>
          <td colspan="3"><input type="text" name="mhssjg" id="mhssjg" /></td>
        </tr>
        <tr>
          <td>8</td>
          <td colspan="5">软弱颗粒含量(%)</td>
          <td colspan="3"><input type="text" name="rrhlzb" id="rrhlzb" /></td>
          <td colspan="3"><input type="text" name="rrklhl" id="rrklhl" readonly="readonly" /></td>
          <td colspan="3"><input type="text" name="rrhljg" id="rrhljg" /></td>
        </tr>
        <tr>
          <td>9</td>
          <td colspan="5">磨光值(%)</td>
          <td colspan="3"><input type="text" name="mgzzb" id="mgzzb" /></td>
          <td colspan="3"><input type="text" name="mgz" id="mgz" /></td>
          <td colspan="3"><input type="text" name="mgzjg" id="mgzjg" /></td>
        </tr>
        <tr>
          <td>10</td>
          <td colspan="5">冲击值(%)</td>
          <td colspan="3"><input type="text" name="cjzzb" id="cjzzb" /></td>
          <td colspan="3"><input type="text" name="cjzpjz" id="cjzpjz" readonly="readonly" /></td>
          <td colspan="3"><input type="text" name="cjzjg" id="cjzjg" /></td>
        </tr>
        <tr>
          <td>11</td>
          <td colspan="14">密度试验结果</td>
        </tr>
        <tr>
          <td colspan="2" width="16%">检测项目</td>
          <td colspan="2" width="8%">技术指标</td>
          <td colspan="2" width="8%">检测结果</td>
          <td width="8%">结果判定</td>
          <td colspan="3" width="16%">检测项目</td>
          <td colspan="2" width="8%">技术指标</td>
          <td colspan="2" width="8%">检测结果</td>
          <td width="8%">结果判定</td>
        </tr>
        <tr>
          <td colspan="2">表观密度(g/cm³)</td>
          <td colspan="2"><input type="text" name="bguanmdzb" id="bguanmdzb" /></td>
          <td colspan="2"><input type="text" name="bguanmdpjz" id="bguanmdpjz" readonly="readonly" /></td>
          <td><input type="text" name="bguanmdjg" id="bguanmdjg" /></td>
          <td colspan="3">吸水率(%)</td>
          <td colspan="2"><input type="text" name="xslzb" id="xslzb" /></td>
          <td colspan="2"><input type="text" name="xslpjz" id="xslpjz" readonly="readonly" /></td>
          <td><input type="text" name="xsljg" id="xsljg" /></td>
        </tr>
        <tr>
          <td colspan="2">表干密度(g/cm³)</td>
          <td colspan="2"><input type="text" name="bganmdzb" id="bganmdzb" /></td>
          <td colspan="2"><input type="text" name="bganmdpjz" id="bganmdpjz" readonly="readonly" /></td>
          <td><input type="text" name="bganmdjg" id="bganmdjg" /></td>
          <td colspan="3">堆积空隙率(%)</td>
          <td colspan="2"><input type="text" name="kxlzb" id="kxlzb" /></td>
          <td colspan="2"><input type="text" name="kxl" id="kxl" readonly="readonly" /></td>
          <td><input type="text" name="kxljg" id="kxljg" /></td>
        </tr>
        <tr>
          <td colspan="2">毛体积密度(g/cm³)</td>
          <td colspan="2"><input type="text" name="mtjmdzb" id="mtjmdzb" /></td>
          <td colspan="2"><input type="text" name="mtjmdpjz" id="mtjmdpjz" readonly="readonly" /></td>
          <td><input type="text" name="mtjmdjg" id="mtjmdjg" /></td>
          <td colspan="3">表观相对密度</td>
          <td colspan="2"><input type="text" name="bgxdmdzb" id="bgxdmdzb" /></td>
          <td colspan="2"><input type="text" name="bgxdmd" id="bgxdmd" readonly="readonly" /></td>
          <td><input type="text" name="bguanxdmdjg" id="bguanxdmdjg" /></td>
        </tr>
        <tr>
          <td colspan="2" class="html">堆积密度(t/cm³)</td>
          <td colspan="2"><input type="text" name="djmdzb" id="djmdzb" /></td>
          <td colspan="2"><input type="text" name="djmdpjz" id="djmdpjz" readonly="readonly" /></td>
          <td><input type="text" name="djmdjg" id="djmdjg" /></td>
          <td colspan="3">表干相对密度</td>
          <td colspan="2"><input type="text" name="bgxdmdzb2" id="bgxdmdzb2" /></td>
          <td colspan="2"><input type="text" name="bganxdmd" id="bganxdmd" readonly="readonly" /></td>
          <td><input type="text" name="bganxdmdjg" id="bganxdmdjg" /></td>
        </tr>
        <tr>
          <td colspan="2" class="html">振实密度(t/cm³)</td>
          <td colspan="2"><input type="text" name="zsmdzb" id="zsmdzb" /></td>
          <td colspan="2"><input type="text" name="zsmdpjz" id="zsmdpjz" readonly="readonly" /></td>
          <td><input type="text" name="zsmdjg" id="zsmdjg" /></td>
          <td colspan="3">毛体积相对密度</td>
          <td colspan="2"><input type="text" name="mtjxdmdzb" id="mtjxdmdzb" /></td>
          <td colspan="2"><input type="text" name="mtjxdmd" id="mtjxdmd" readonly="readonly" /></td>
          <td><input type="text" name="mtjxdmdjg" id="mtjxdmdjg" /></td>
        </tr>
        <tr>
          <td colspan="2">捣实密度(g/cm³)</td>
          <td colspan="2"><input type="text" name="dsmdzb" id="dsmdzb" /></td>
          <td colspan="2"><input type="text" name="dsmd" id="dsmd" /></td>
          <td><input type="text" name="dsmdjg" id="dsmdjg" /></td>
          <td colspan="3">/</td>
          <td colspan="2">/</td>
          <td colspan="2">/</td>
          <td>/</td>
        </tr>
        <tr>
          <td>12</td>
          <td colspan="14">颗粒分析</td>
        </tr>
      </table>
      <table class="layui-table"
             style="border: 2px solid #000;border-top:none;margin-top:-1px; box-sizing: border-box;">
        <tr>
          <td colspan="3" width="20%">筛孔尺寸(mm)</td>
          <td><input type="text" name="skcc01" id="skcc01" readonly="readonly" /></td>
          <td><input type="text" name="skcc02" id="skcc02" readonly="readonly" /></td>
          <td><input type="text" name="skcc03" id="skcc03" readonly="readonly" /></td>
          <td><input type="text" name="skcc04" id="skcc04" readonly="readonly" /></td>
          <td><input type="text" name="skcc05" id="skcc05" readonly="readonly" /></td>
          <td><input type="text" name="skcc06" id="skcc06" readonly="readonly" /></td>
          <td><input type="text" name="skcc07" id="skcc07" readonly="readonly" /></td>
          <td><input type="text" name="skcc08" id="skcc08" readonly="readonly" /></td>
          <td><input type="text" name="skcc09" id="skcc09" readonly="readonly" /></td>
          <td><input type="text" name="skcc10" id="skcc10" readonly="readonly" /></td>
          <td><input type="text" name="skcc11" id="skcc11" readonly="readonly" /></td>
          <td><input type="text" name="skcc12" id="skcc12" readonly="readonly" /></td>
          <td><input type="text" name="skcc13" id="skcc13" readonly="readonly" /></td>
          <td><input type="text" name="skcc14" id="skcc14" readonly="readonly" /></td>
          <td><input type="text" name="skcc15" id="skcc15" readonly="readonly" /></td>
        </tr>
        <tr>
          <td colspan="3">标准累计筛余上限(%)</td>
          <td><input type="text" name="bztglfwsx01" id="bztglfwsx01" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwsx02" id="bztglfwsx02" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwsx03" id="bztglfwsx03" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwsx04" id="bztglfwsx04" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwsx05" id="bztglfwsx05" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwsx06" id="bztglfwsx06" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwsx07" id="bztglfwsx07" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwsx08" id="bztglfwsx08" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwsx09" id="bztglfwsx09" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwsx10" id="bztglfwsx10" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwsx11" id="bztglfwsx11" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwsx12" id="bztglfwsx12" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwsx13" id="bztglfwsx13" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwsx14" id="bztglfwsx14" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwsx15" id="bztglfwsx15" readonly="readonly" /></td>
        </tr>
        <tr>
          <td colspan="3">标准累计筛余下限(%)</td>
          <td><input type="text" name="bztglfwxx01" id="bztglfwxx01" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwxx02" id="bztglfwxx02" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwxx03" id="bztglfwxx03" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwxx04" id="bztglfwxx04" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwxx05" id="bztglfwxx05" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwxx06" id="bztglfwxx06" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwxx07" id="bztglfwxx07" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwxx08" id="bztglfwxx08" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwxx09" id="bztglfwxx09" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwxx10" id="bztglfwxx10" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwxx11" id="bztglfwxx11" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwxx12" id="bztglfwxx12" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwxx13" id="bztglfwxx13" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwxx14" id="bztglfwxx14" readonly="readonly" /></td>
          <td><input type="text" name="bztglfwxx15" id="bztglfwxx15" readonly="readonly" /></td>
        </tr>
        <tr>
          <td colspan="3">实际累计筛余(%)</td>
          <td><input type="text" name="pjljsybfl01" id="pjljsybfl01" readonly="readonly" /></td>
          <td><input type="text" name="pjljsybfl02" id="pjljsybfl02" readonly="readonly" /></td>
          <td><input type="text" name="pjljsybfl03" id="pjljsybfl03" readonly="readonly" /></td>
          <td><input type="text" name="pjljsybfl04" id="pjljsybfl04" readonly="readonly" /></td>
          <td><input type="text" name="pjljsybfl05" id="pjljsybfl05" readonly="readonly" /></td>
          <td><input type="text" name="pjljsybfl06" id="pjljsybfl06" readonly="readonly" /></td>
          <td><input type="text" name="pjljsybfl07" id="pjljsybfl07" readonly="readonly" /></td>
          <td><input type="text" name="pjljsybfl08" id="pjljsybfl08" readonly="readonly" /></td>
          <td><input type="text" name="pjljsybfl09" id="pjljsybfl09" readonly="readonly" /></td>
          <td><input type="text" name="pjljsybfl10" id="pjljsybfl10" readonly="readonly" /></td>
          <td><input type="text" name="pjljsybfl11" id="pjljsybfl11" readonly="readonly" /></td>
          <td><input type="text" name="pjljsybfl12" id="pjljsybfl12" readonly="readonly" /></td>
          <td><input type="text" name="pjljsybfl13" id="pjljsybfl13" readonly="readonly" /></td>
          <td><input type="text" name="pjljsybfl14" id="pjljsybfl14" readonly="readonly" /></td>
          <td><input type="text" name="pjljsybfl15" id="pjljsybfl15" readonly="readonly" /></td>
        </tr>
        <tr style="display: none;">
          <td colspan="3">符合粒级</td>
          <td colspan="6"><input type="text" name="fhlj" id="fhlj" /></td>
          <td colspan="3">最大粒级(mm)</td>
          <td colspan="6"><input type="text" name="zdlj" id="zdlj" /></td>
        </tr>

      </table>
      <table class="layui-table"
             style="border: 2px solid #000;border-top: none;margin-top: -2px;box-sizing: border-box">
        <tr>
          <td style="border-right:none;width: 62px;" align="right" valign="top">检测结论：</td>
          <td style="border-left: none;height: 62px;">
            <textarea name="jcjl" id="jcjl"></textarea>
          </td>
        </tr>
      </table>

    </div>






  </div>


</template>

<script>
import {getAction, postAction} from "@api/manage";

export default {
  name: 'expForm',
  data() {
    return {
      modal: {
        width: 100
      },
      table: {
        bordered: true
      }
    }
  },
  methods: {
    dateChange(date, dateString) {
      console.log('date and dateString:', date, dateString);
    }
  }
};
</script>
<style scoped>
</style>
