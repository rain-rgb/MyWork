<template>
  <div>
    <form id="form" action="" method="post" onsubmit="return false;">
      <div class="paperA4">
        <div class="tableHeader"></div>
        <div class="tableContent">
          <table class="layui-table">
            <tr>
              <td width="10%">工程名称</td>
              <td><textarea type="text" name="gongchengmingcheng" id="gongchengmingcheng"></textarea></td>
              <td width="16%">试验日期</td>
              <td><input type="text" name="shiyanriqi" id="shiyanriqi"/></td>
            </tr>
            <tr>
              <td>桩号及部位</td>
              <td><textarea id="sampleGcbw" name="sampleGcbw"></textarea></td>
              <td>仪器名称及型号</td>
              <td style="height: 56px"><textarea name="yiqishebei" id="yiqishebei"></textarea></td>
            </tr>
          </table>
          <table class="layui-table next">
            <tr>
              <td width="10%">养护条件</td>
              <td colspan="3"><input type="text" name="sampleYangHuTiaoJian" id="sampleYangHuTiaoJian"></td>
              <td>制件说明</td>
              <td colspan="3"><input type="text" name="sampleJiaoBanFangShi" id="sampleJiaoBanFangShi"></td>
              <td>强度等级</td>
              <td colspan="2"><input type="text" name="sampleQiangDuDengJi" id="sampleQiangDuDengJi"></td>
            </tr>
            <tr>
              <td rowspan="2" width="10%">试件编号</td>
              <td rowspan="2" width="10%">制件日期</td>
              <td rowspan="2" width="6%">期龄(d)</td>
              <td rowspan="2" width="10%">试验日期</td>
              <td rowspan="2">试件尺寸(mm)</td>
              <td rowspan="2">破坏荷载(KN)</td>
              <td colspan="2">抗折强度(MPa)</td>
              <td rowspan="2">破坏荷载(KN)</td>
              <td colspan="2">抗压强度(MPa)</td>
            </tr>
            <tr>
              <td>单值</td>
              <td>测定值</td>
              <td>单值</td>
              <td>测定值</td>
            </tr>
            <tr>
              <td rowspan="6"><textarea name="bianhao1" id="bianhao1"></textarea></td>
              <td rowspan="6"><input type="text" name="zhijianriqi1" id="zhijianriqi1"/></td>
              <td rowspan="6"><input type="text" name="lingqi1" id="lingqi1"/></td>
              <td rowspan="6"><input type="text" name="poxrq1" id="poxrq1"/></td>
              <td rowspan="6"><select name="chicun1" id="chicun1">
                <option value=""></option>
                <option value="40X40X160">40X40X160</option>
              </select></td>
              <td rowspan="2"><input type="text" name="kangzhepohuaihezai11" id="kangzhepohuaihezai11" enterindex="1"/></td>
              <td rowspan="2"><input type="text" name="kangzhedanzhi11" id="kangzhedanzhi11"/></td>
              <td rowspan="6"><input type="text" name="kangzhecedingzhi1" id="kangzhecedingzhi1"/></td>
              <td><input type="text" name="kangyapohuaihezai11" id="kangyapohuaihezai11" enterindex="4"/></td>
              <td><input type="text" name="kangyadanzhi11" id="kangyadanzhi11"/></td>
              <td rowspan="6"><input type="text" name="kangyacedingzhi1" id="kangyacedingzhi1"/></td>
            </tr>
            <tr>
              <td><input type="text" name="kangyapohuaihezai12" id="kangyapohuaihezai12" enterindex="5"/></td>
              <td><input type="text" name="kangyadanzhi12" id="kangyadanzhi12"/></td>
            </tr>
            <tr>
              <td rowspan="2"><input type="text" name="kangzhepohuaihezai12" id="kangzhepohuaihezai12" enterindex="2"/></td>
              <td rowspan="2"><input type="text" name="kangzhedanzhi12" id="kangzhedanzhi12"/></td>
              <td><input type="text" name="kangyapohuaihezai13" id="kangyapohuaihezai13" enterindex="6"/></td>
              <td><input type="text" name="kangyadanzhi13" id="kangyadanzhi13"/></td>
            </tr>
            <tr>
              <td><input type="text" name="kangyapohuaihezai14" id="kangyapohuaihezai14" enterindex="7"/></td>
              <td><input type="text" name="kangyadanzhi14" id="kangyadanzhi14"/></td>
            </tr>
            <tr>
              <td rowspan="2"><input type="text" name="kangzhepohuaihezai13" id="kangzhepohuaihezai13" enterindex="3"/></td>
              <td rowspan="2"><input type="text" name="kangzhedanzhi13" id="kangzhedanzhi13"/></td>
              <td><input type="text" name="kangyapohuaihezai15" id="kangyapohuaihezai15" enterindex="8"/></td>
              <td><input type="text" name="kangyadanzhi15" id="kangyadanzhi15"/></td>
            </tr>
            <tr>
              <td><input type="text" name="kangyapohuaihezai16" id="kangyapohuaihezai16" enterindex="9"/></td>
              <td><input type="text" name="kangyadanzhi16" id="kangyadanzhi16"/></td>
            </tr>
            <tr>
              <td rowspan="6"><textarea name="bianhao2" id="bianhao2"></textarea></td>
              <td rowspan="6"><input type="text" name="zhijianriqi2" id="zhijianriqi2" enterindex="10"/></td>
              <td rowspan="6"><input type="text" name="lingqi2" id="lingqi2" enterindex="11"/></td>
              <td rowspan="6"><input type="text" name="poxrq2" id="poxrq2"/></td>
              <td rowspan="6"><select name="chicun2" id="chicun2">
                <option value=""></option>
                <option value="40X40X160">40X40X160</option>
              </select></td>
              <td rowspan="2"><input type="text" name="kangzhepohuaihezai21" id="kangzhepohuaihezai21" enterindex="12"/></td>
              <td rowspan="2"><input type="text" name="kangzhedanzhi21" id="kangzhedanzhi21"/></td>
              <td rowspan="6"><input type="text" name="kangzhecedingzhi2" id="kangzhecedingzhi2"/></td>
              <td><input type="text" name="kangyapohuaihezai21" id="kangyapohuaihezai21" enterindex="15"/></td>
              <td><input type="text" name="kangyadanzhi21" id="kangyadanzhi21"/></td>
              <td rowspan="6"><input type="text" name="kangyacedingzhi2" id="kangyacedingzhi2"/></td>
            </tr>
            <tr>
              <td><input type="text" name="kangyapohuaihezai22" id="kangyapohuaihezai22" enterindex="16"/></td>
              <td><input type="text" name="kangyadanzhi22" id="kangyadanzhi22"/></td>
            </tr>
            <tr>
              <td rowspan="2"><input type="text" name="kangzhepohuaihezai22" id="kangzhepohuaihezai22" enterindex="13"/></td>
              <td rowspan="2"><input type="text" name="kangzhedanzhi22" id="kangzhedanzhi22"/></td>
              <td><input type="text" name="kangyapohuaihezai23" id="kangyapohuaihezai23" enterindex="17"/></td>
              <td><input type="text" name="kangyadanzhi23" id="kangyadanzhi23"/></td>
            </tr>
            <tr>
              <td><input type="text" name="kangyapohuaihezai24" id="kangyapohuaihezai24"/></td>
              <td><input type="text" name="kangyadanzhi24" id="kangyadanzhi24"/></td>
            </tr>
            <tr>
              <td rowspan="2"><input type="text" name="kangzhepohuaihezai23" id="kangzhepohuaihezai23" enterindex="14"/></td>
              <td rowspan="2"><input type="text" name="kangzhedanzhi23" id="kangzhedanzhi23"/></td>
              <td><input type="text" name="kangyapohuaihezai25" id="kangyapohuaihezai25" enterindex="18"/></td>
              <td><input type="text" name="kangyadanzhi25" id="kangyadanzhi25"/></td>
            </tr>
            <tr>
              <td><input type="text" name="kangyapohuaihezai26" id="kangyapohuaihezai26" enterindex="19"/></td>
              <td><input type="text" name="kangyadanzhi26" id="kangyadanzhi26"/></td>
            </tr>
            <tr>
              <td rowspan="6"><textarea name="bianhao3" id="bianhao3"></textarea></td>
              <td rowspan="6"><input type="text" name="zhijianriqi3" id="zhijianriqi3" enterindex="20"/></td>
              <td rowspan="6"><input type="text" name="lingqi3" id="lingqi3" enterindex="21"/></td>
              <td rowspan="6"><input type="text" name="poxrq3" id="poxrq3"/></td>
              <td rowspan="6"><select name="chicun3" id="chicun3">
                <option value=""></option>
                <option value="40X40X160">40X40X160</option>
              </select></td>
              <td rowspan="2"><input type="text" name="kangzhepohuaihezai31" id="kangzhepohuaihezai31" enterindex="22"/></td>
              <td rowspan="2"><input type="text" name="kangzhedanzhi31" id="kangzhedanzhi31"/></td>
              <td rowspan="6"><input type="text" name="kangzhecedingzhi3" id="kangzhecedingzhi3"/></td>
              <td><input type="text" name="kangyapohuaihezai31" id="kangyapohuaihezai31" enterindex="25"/></td>
              <td><input type="text" name="kangyadanzhi31" id="kangyadanzhi31"/></td>
              <td rowspan="6"><input type="text" name="kangyacedingzhi3" id="kangyacedingzhi3"/></td>
            </tr>
            <tr>
              <td><input type="text" name="kangyapohuaihezai32" id="kangyapohuaihezai32" enterindex="26"/></td>
              <td><input type="text" name="kangyadanzhi32" id="kangyadanzhi32"/></td>
            </tr>
            <tr>
              <td rowspan="2"><input type="text" name="kangzhepohuaihezai32" id="kangzhepohuaihezai32" enterindex="23"/></td>
              <td rowspan="2"><input type="text" name="kangzhedanzhi32" id="kangzhedanzhi32"/></td>
              <td><input type="text" name="kangyapohuaihezai33" id="kangyapohuaihezai33" enterindex="27"/></td>
              <td><input type="text" name="kangyadanzhi33" id="kangyadanzhi33"/></td>
            </tr>
            <tr>
              <td><input type="text" name="kangyapohuaihezai34" id="kangyapohuaihezai34" enterindex="28"/></td>
              <td><input type="text" name="kangyadanzhi34" id="kangyadanzhi34"/></td>
            </tr>
            <tr>
              <td rowspan="2"><input type="text" name="kangzhepohuaihezai33" id="kangzhepohuaihezai33" enterindex="24"/></td>
              <td rowspan="2"><input type="text" name="kangzhedanzhi33" id="kangzhedanzhi33"/></td>
              <td><input type="text" name="kangyapohuaihezai35" id="kangyapohuaihezai35" enterindex="29"/></td>
              <td><input type="text" name="kangyadanzhi35" id="kangyadanzhi35"/></td>
            </tr>
            <tr>
              <td><input type="text" name="kangyapohuaihezai36" id="kangyapohuaihezai36" enterindex="30"/></td>
              <td><input type="text" name="kangyadanzhi36" id="kangyadanzhi36"/></td>
            </tr>
            <tr style="display: none">
              <td><input type="text" name="kangzheqducedzhi" id="kangzheqducedzhi" BG=""/></td>
              <td><input type="text" name="kangyaqducedzhi" id="kangyaqducedzhi" BG=""/></td>
            </tr>
          </table>
        </div>
        <div class="tableRemark"></div>
      </div>
    </form>
  </div>

</template>

<script>
import {getAction, postAction} from "@api/manage";

export default {
  name: 'JJ0520b',
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
