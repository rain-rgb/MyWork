<template>
  <div>
    <form id="form" action="" method="post" onsubmit="return false;" >
      <!--引用公用表头部分 固定高度234px-->
      <div class="tableHeader" ></div>
      <!--表内容部分 固定高度 668px-->
      <div class="tableContent" >
        <table class="layui-table" >
          <tr>
            <td>工程名称</td>
            <td><textarea type="text" name="gongchengmingcheng" id="gongchengmingcheng"></textarea></td>
            <td>试验日期</td>
            <td><input type="text" name="shiyanriqi" id="shiyanriqi"/></td>
          </tr>
          <tr>
            <td>桩号及部位</td>
            <td><textarea id="zhuanghaojibuwei" name="zhuanghaojibuwei" BG=""></textarea></td>
            <td>仪器名称及型号</td>
            <td><textarea name="yiqishebei" id="yiqishebei"></textarea></td>
          </tr>
          <tr>
            <td>养护条件</td>
            <td><input type="text" id="sampleYangHuTiaoJian" name="sampleYangHuTiaoJian"></td>
            <td>制件说明</td>
            <td><input type="text" id="zhijianshuoming" name="zhijianshuoming" BG=""></td>


          </tr>
        </table>
        <table class="layui-table next ">
          <tr>
            <td rowspan="2">试件编号</td>

            <td rowspan="2" width="12%">制件日期</td>

            <td rowspan="2" width="12%">试压日期</td>
            <td rowspan="2">期龄(d)</td>
            <td rowspan="2" style="width:100px">试件尺寸(mm)</td>
            <td rowspan="2" >外观描述</td>
            <td rowspan="2" style="width:50px">破坏荷载(kN)</td>
            <td colspan="2">抗压强度(MPa)</td>
            <td rowspan="2">设计强度等级</td>
          </tr>
          <tr>
            <td style="width:50px">单值</td>
            <td style="width:50px">测定值</td>
          </tr>
          <tr>
            <td rowspan="3"><textarea BG="" id='shijbh' name='shijbh'></textarea></td>

            <td rowspan="3"><input type="text" name="zhijrq" BG="" id="zhijrq" onchange="syrq()"/></td>

            <td rowspan="3"><input type='text' id='shiyrq' name='shiyrq' readonly="readonly" BG=""/></td>
            <td rowspan="3"><input type='text' BG="" id='sampleLingQi' name='sampleLingQi' onchange="syrq()"/></td>
            <td rowspan="3"><input type='text' BG="" id='shijcc' name='shijcc'/></td>
            <td rowspan="3" class="pad"><textarea id="jiegbw" name="jiegbw" BG=""></textarea></td>
            <td><input type='text' BG="" id='pohhz1' onchange="xiuyue(this.id,this.value,2);"
                       name='pohhz1'/></td>
            <td><input type='text' readonly="readonly" BG="" id='kangyqddz1' name='kangyqddz1'/></td>
            <td rowspan="3"><input type='text' readonly="readonly" BG="" id='kangyqdpingjz'
                                   name='kangyqdpingjz'/></td>
            <td rowspan="3"><input type='text' BG="" id='shejqddj1' name='shejqddj1'/></td>
          </tr>
          <tr>
            <td><input type='text' BG="" id='pohhz2' onchange="xiuyue(this.id,this.value,2);"
                       name='pohhz2'/></td>
            <td><input type='text' readonly="readonly" BG="" id='kangyqddz2' name='kangyqddz2'/></td>
          </tr>
          <tr>
            <td><input type='text' BG="" id='pohhz3' onchange="xiuyue(this.id,this.value,2);"
                       name='pohhz3'/></td>
            <td><input type='text' readonly="readonly" BG="" id='kangyqddz3' name='kangyqddz3'/></td>
          </tr>
          <tr>
            <td rowspan="3"><textarea BG="" id='shijbh2' name='shijbh2'></textarea></td>

            <td rowspan="3"><input type="text" BG="" name="zhijrq2" id="zhijrq2" onchange="syrq()"/></td>

            <td rowspan="3"><input type='text' id='shiyrq2' name='shiyrq2' readonly="readonly" BG=""/></td>
            <td rowspan="3"><input type='text' BG="" id='lingq2' name='lingq2' onchange="syrq()"/></td>
            <td rowspan="3"><input type='text' BG="" id='shijcc2' name='shijcc2'/></td>
            <td rowspan="3" class="pad"><textarea id="jiegbw2" name="jiegbw2" BG=""
                                                  style="width: 100%;;"></textarea></td>
            <td><input type='text' BG="" id='pohhzer1' onchange="xiuyue(this.id,this.value,2);"
                       name='pohhzer1'/></td>
            <td><input type='text' readonly="readonly" BG="" id='kangyqddzer1' name='kangyqddzer1'/></td>
            <td rowspan="3"><input type='text' readonly="readonly" BG="" id='kangyqdpingjzer'
                                   name='kangyqdpingjzer'/></td>
            <td  rowspan="3"><input type='text' BG="" id='shejqddj2' name='shejqddj2'/></td>
          </tr>
          <tr>
            <td><input type='text' BG="" id='pohhzer2' onchange="xiuyue(this.id,this.value,2);"
                       name='pohhzer2'/></td>
            <td><input type='text' readonly="readonly" BG="" id='kangyqddzer2' name='kangyqddzer2'/></td>
          </tr>
          <tr>
            <td><input type='text' BG="" id='pohhzer3' onchange="xiuyue(this.id,this.value,2);"
                       name='pohhzer3'/></td>
            <td><input type='text' readonly="readonly" BG="" id='kangyqddzer3' name='kangyqddzer3'/></td>
          </tr>
          <tr>
            <td rowspan="3"><textarea BG="" id='shijbh3' name='shijbh3'></textarea></td>

            <td rowspan="3"><input type="text" name="zhijrq3" id="zhijrq3" onchange="syrq()"/></td>

            <td rowspan="3"><input type='text' id='shiyrq3' name='shiyrq3' readonly="readonly" BG=""/></td>
            <td rowspan="3"><input type='text' BG="" id='lingq3' name='lingq3' onchange="syrq()"/></td>
            <td rowspan="3"><input type='text' BG="" id='shijcc3' name='shijcc3'/></td>
            <td rowspan="3" class="pad"><textarea id="jiegbw3" name="jiegbw3" BG=""
                                                  style="width: 100%;;"></textarea></td>
            <td><input type='text' BG="" id='pohhzsan1' onchange="xiuyue(this.id,this.value,2);"
                       name='pohhzsan1'/></td>
            <td><input type='text' readonly="readonly" BG="" id='kangyqddzsan1' name='kangyqddzsan1'/></td>
            <td rowspan="3"><input type='text' readonly="readonly" BG="" id='kangyqdpingjzsan'
                                   name='kangyqdpingjzsan'/></td>
            <td  rowspan="3"><input type='text' BG="" id='shejqddj3' name='shejqddj3'/></td>
          </tr>
          <tr>
            <td><input type='text' BG="" id='pohhzsan2' onchange="xiuyue(this.id,this.value,2);"
                       name='pohhzsan2'/></td>
            <td><input type='text' readonly="readonly" BG="" id='kangyqddzsan2' name='kangyqddzsan2'/></td>
          </tr>
          <tr>
            <td><input type='text' BG="" id='pohhzsan3' onchange="xiuyue(this.id,this.value,2);"
                       name='pohhzsan3'/></td>
            <td><input type='text' readonly="readonly" BG="" id='kangyqddzsan3' name='kangyqddzsan3'/></td>
          </tr>
          <tr>
            <td rowspan="3"><textarea BG="" id='shijbh4' name='shijbh4'></textarea></td>

            <td rowspan="3"><input type="text" name="zhijrq4" id="zhijrq4" onchange="syrq()"/></td>

            <td rowspan="3"><input type='text' id='shiyrq4' name='shiyrq4' readonly="readonly" BG=""/></td>
            <td rowspan="3"><input type='text' BG="" id='lingq4' name='lingq4' onchange="syrq()"/></td>
            <td rowspan="3"><input type='text' BG="" id='shijcc4' name='shijcc4'/></td>
            <td rowspan="3"><textarea id="jiegbw4" name="jiegbw4" BG="" style="width: 100%;;"></textarea></td>
            <td><input type='text' BG="" id='pohhzsi1' onchange="xiuyue(this.id,this.value,2);"
                       name='pohhzsi1'/></td>
            <td><input type='text' readonly="readonly" BG="" id='kangyqddzsi1' name='kangyqddzsi1'/></td>
            <td rowspan="3"><input type='text' readonly="readonly" BG="" BG="" id='kangyqdpingjzsi'
                                   name='kangyqdpingjzsi'/></td>
            <td  rowspan="3"><input type='text' BG="" id='shejqddj4' name='shejqddj4'/></td>
          </tr>
          <tr>
            <td><input type='text' BG="" id='pohhzsi2' onchange="xiuyue(this.id,this.value,2);"
                       name='pohhzsi2'/></td>
            <td><input type='text' readonly="readonly" BG="" id='kangyqddzsi2' name='kangyqddzsi2'/></td>
          </tr>
          <tr>
            <td><input type='text' BG="" id='pohhzsi3' onchange="xiuyue(this.id,this.value,2);"
                       name='pohhzsi3'/></td>
            <td><input type='text' readonly="readonly" BG="" id='kangyqddzsi3' name='kangyqddzsi3'/></td>
          </tr>
        </table>
      </div>
      <div class="tableRemark" ></div>
    </form>
  </div>

</template>

<script>
import {getAction, postAction} from "@api/manage";

export default {
  name: 'JB0105012',
  data() {
    return {
    }
  },
  methods: {
  }
};
</script>
<style scoped>
</style>
