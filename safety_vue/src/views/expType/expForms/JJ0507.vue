<template>
  <div>
    <form id="form" action="" method="post" onsubmit="return false;" class="">
      <div class="tableHeader"></div>
      <div class="tableContent">
        <table class="layui-table">
          <tr>
            <td>样品名称</td>
            <td><input type="text" name="sampleName" id="sampleName"></td>
            <td>试验日期</td>
            <td><input type="text" name="shiyanriqi" id="shiyanriqi"></td>
          </tr>
          <tr>
            <td>样品来源</td>
            <td><input type="text" name="sampleQuYangDiDian" id="sampleQuYangDiDian"></td>
            <td>用途</td>
            <td><input type="text" name="sampleGcbw" id="sampleGcbw"></td>
          </tr>
          <tr>
            <td>试验依据</td>
            <td colspan="3"><input type="text" name="shiyanyiju" id="shiyanyiju" value=""/></td>
          </tr>
        </table>
        <table class="layui-table next">
          <tr>
            <td style="text-align: left;">
              &nbsp;
              [<input type="text" name="ss5mm" id="ss5mm" style="width: 50px;">]（5mm）湿筛
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              [<input type="text" name="sjdphbbh" id="sjdphbbh" style="width: 50px;">]按砼中砂浆的配合比直接称料拌和
            </td>
          </tr>
          <tr>
            <td style="text-align: left;">
              &nbsp;
              [<input type="text" name="zdt" id="zdt" style="width: 50px;">]振动台
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              [<input type="text" name="rgcd" id="rgcd" style="width: 50px;">]人工插捣
            </td>
          </tr>
        </table>
        <table class="layui-table next">
          <tr>
            <td colspan="3">试验次数</td>
            <td>1</td>
            <td>2</td>
            <td>3</td>
            <td>4</td>
            <td>5</td>
            <td>6</td>
            <td>7</td>
            <td>8</td>
          </tr>
          <tr>
            <td rowspan="8" style="width: 80px;">
              试样（1）<br/>F0=
              <input type="text" name="f0sy1" id="f0sy1" style="width: 40px; height: 23px;"><br/>
              （N）
            </td>
            <td colspan="2">时间（h）</td>
            <td><input type="text" id="sj1_d1" name="sj1_d1"></td>
            <td><input type="text" id="sj1_d2" name="sj1_d2"></td>
            <td><input type="text" id="sj1_d3" name="sj1_d3"></td>
            <td><input type="text" id="sj1_d4" name="sj1_d4"></td>
            <td><input type="text" id="sj1_d5" name="sj1_d5"></td>
            <td><input type="text" id="sj1_d6" name="sj1_d6"></td>
            <td><input type="text" id="sj1_d7" name="sj1_d7"></td>
            <td><input type="text" id="sj1_d8" name="sj1_d8"></td>
          </tr>
          <tr>
            <td colspan="2">气温（℃）</td>
            <td><input type="text" id="qw1_d1" name="qw1_d1"></td>
            <td><input type="text" id="qw1_d2" name="qw1_d2"></td>
            <td><input type="text" id="qw1_d3" name="qw1_d3"></td>
            <td><input type="text" id="qw1_d4" name="qw1_d4"></td>
            <td><input type="text" id="qw1_d5" name="qw1_d5"></td>
            <td><input type="text" id="qw1_d6" name="qw1_d6"></td>
            <td><input type="text" id="qw1_d7" name="qw1_d7"></td>
            <td><input type="text" id="qw1_d8" name="qw1_d8"></td>
          </tr>
          <tr>
            <td colspan="2">A（mm2）</td>
            <td><input type="text" id="a1_d1" name="a1_d1"></td>
            <td><input type="text" id="a1_d2" name="a1_d2"></td>
            <td><input type="text" id="a1_d3" name="a1_d3"></td>
            <td><input type="text" id="a1_d4" name="a1_d4"></td>
            <td><input type="text" id="a1_d5" name="a1_d5"></td>
            <td><input type="text" id="a1_d6" name="a1_d6"></td>
            <td><input type="text" id="a1_d7" name="a1_d7"></td>
            <td><input type="text" id="a1_d8" name="a1_d8"></td>
          </tr>
          <tr>
            <td rowspan="3">读数F1（N）</td>
            <td>点1</td>
            <td><input type="text" id="ds1_d11" name="ds1_d11"></td>
            <td><input type="text" id="ds1_d21" name="ds1_d21"></td>
            <td><input type="text" id="ds1_d31" name="ds1_d31"></td>
            <td><input type="text" id="ds1_d41" name="ds1_d41"></td>
            <td><input type="text" id="ds1_d51" name="ds1_d51"></td>
            <td><input type="text" id="ds1_d61" name="ds1_d61"></td>
            <td><input type="text" id="ds1_d71" name="ds1_d71"></td>
            <td><input type="text" id="ds1_d81" name="ds1_d81"></td>
          </tr>
          <tr>
            <td>点2</td>
            <td><input type="text" id="ds1_d12" name="ds1_d12"></td>
            <td><input type="text" id="ds1_d22" name="ds1_d22"></td>
            <td><input type="text" id="ds1_d32" name="ds1_d32"></td>
            <td><input type="text" id="ds1_d42" name="ds1_d42"></td>
            <td><input type="text" id="ds1_d52" name="ds1_d52"></td>
            <td><input type="text" id="ds1_d62" name="ds1_d62"></td>
            <td><input type="text" id="ds1_d72" name="ds1_d72"></td>
            <td><input type="text" id="ds1_d82" name="ds1_d82"></td>
          </tr>
          <tr>
            <td>平均值</td>
            <td><input type="text" id="pjz1_d1" name="pjz1_d1"></td>
            <td><input type="text" id="pjz1_d2" name="pjz1_d2"></td>
            <td><input type="text" id="pjz1_d3" name="pjz1_d3"></td>
            <td><input type="text" id="pjz1_d4" name="pjz1_d4"></td>
            <td><input type="text" id="pjz1_d5" name="pjz1_d5"></td>
            <td><input type="text" id="pjz1_d6" name="pjz1_d6"></td>
            <td><input type="text" id="pjz1_d7" name="pjz1_d7"></td>
            <td><input type="text" id="pjz1_d8" name="pjz1_d8"></td>
          </tr>
          <tr>
            <td colspan="2">平均贯入力F=F1-F0（N）</td>
            <td><input type="text" id="pjgr1_d1" name="pjgr1_d1"></td>
            <td><input type="text" id="pjgr1_d2" name="pjgr1_d2"></td>
            <td><input type="text" id="pjgr1_d3" name="pjgr1_d3"></td>
            <td><input type="text" id="pjgr1_d4" name="pjgr1_d4"></td>
            <td><input type="text" id="pjgr1_d5" name="pjgr1_d5"></td>
            <td><input type="text" id="pjgr1_d6" name="pjgr1_d6"></td>
            <td><input type="text" id="pjgr1_d7" name="pjgr1_d7"></td>
            <td><input type="text" id="pjgr1_d8" name="pjgr1_d8"></td>
          </tr>
          <tr>
            <td colspan="2">P=F/A（MPa）</td>
            <td><input type="text" id="p1_d1" name="p1_d1"></td>
            <td><input type="text" id="p1_d2" name="p1_d2"></td>
            <td><input type="text" id="p1_d3" name="p1_d3"></td>
            <td><input type="text" id="p1_d4" name="p1_d4"></td>
            <td><input type="text" id="p1_d5" name="p1_d5"></td>
            <td><input type="text" id="p1_d6" name="p1_d6"></td>
            <td><input type="text" id="p1_d7" name="p1_d7"></td>
            <td><input type="text" id="p1_d8" name="p1_d8"></td>
          </tr>
          <tr>
            <td rowspan="8" style="width: 80px;">
              试样（2）<br/>F0=
              <input type="text" name="f0sy2" id="f0sy2" style="width: 40px; height: 23px;"><br/>
              （N）
            </td>
            <td colspan="2">时间（h）</td>
            <td><input type="text" id="sj2_d1" name="sj2_d1"></td>
            <td><input type="text" id="sj2_d2" name="sj2_d2"></td>
            <td><input type="text" id="sj2_d3" name="sj2_d3"></td>
            <td><input type="text" id="sj2_d4" name="sj2_d4"></td>
            <td><input type="text" id="sj2_d5" name="sj2_d5"></td>
            <td><input type="text" id="sj2_d6" name="sj2_d6"></td>
            <td><input type="text" id="sj2_d7" name="sj2_d7"></td>
            <td><input type="text" id="sj2_d8" name="sj2_d8"></td>
          </tr>
          <tr>
            <td colspan="2">气温（℃）</td>
            <td><input type="text" id="qw2_d1" name="qw2_d1"></td>
            <td><input type="text" id="qw2_d2" name="qw2_d2"></td>
            <td><input type="text" id="qw2_d3" name="qw2_d3"></td>
            <td><input type="text" id="qw2_d4" name="qw2_d4"></td>
            <td><input type="text" id="qw2_d5" name="qw2_d5"></td>
            <td><input type="text" id="qw2_d6" name="qw2_d6"></td>
            <td><input type="text" id="qw2_d7" name="qw2_d7"></td>
            <td><input type="text" id="qw2_d8" name="qw2_d8"></td>
          </tr>
          <tr>
            <td colspan="2">A（mm2）</td>
            <td><input type="text" id="a2_d1" name="a2_d1"></td>
            <td><input type="text" id="a2_d2" name="a2_d2"></td>
            <td><input type="text" id="a2_d3" name="a2_d3"></td>
            <td><input type="text" id="a2_d4" name="a2_d4"></td>
            <td><input type="text" id="a2_d5" name="a2_d5"></td>
            <td><input type="text" id="a2_d6" name="a2_d6"></td>
            <td><input type="text" id="a2_d7" name="a2_d7"></td>
            <td><input type="text" id="a2_d8" name="a2_d8"></td>
          </tr>
          <tr>
            <td rowspan="3">读数F1（N）</td>
            <td>点1</td>
            <td><input type="text" id="ds2_d11" name="ds2_d11"></td>
            <td><input type="text" id="ds2_d21" name="ds2_d21"></td>
            <td><input type="text" id="ds2_d31" name="ds2_d31"></td>
            <td><input type="text" id="ds2_d41" name="ds2_d41"></td>
            <td><input type="text" id="ds2_d51" name="ds2_d51"></td>
            <td><input type="text" id="ds2_d61" name="ds2_d61"></td>
            <td><input type="text" id="ds2_d71" name="ds2_d71"></td>
            <td><input type="text" id="ds2_d81" name="ds2_d81"></td>
          </tr>
          <tr>
            <td>点2</td>
            <td><input type="text" id="ds2_d12" name="ds2_d12"></td>
            <td><input type="text" id="ds2_d22" name="ds2_d22"></td>
            <td><input type="text" id="ds2_d32" name="ds2_d32"></td>
            <td><input type="text" id="ds2_d42" name="ds2_d42"></td>
            <td><input type="text" id="ds2_d52" name="ds2_d52"></td>
            <td><input type="text" id="ds2_d62" name="ds2_d62"></td>
            <td><input type="text" id="ds2_d72" name="ds2_d72"></td>
            <td><input type="text" id="ds2_d82" name="ds2_d82"></td>
          </tr>
          <tr>
            <td>平均值</td>
            <td><input type="text" id="pjz2_d1" name="pjz2_d1"></td>
            <td><input type="text" id="pjz2_d2" name="pjz2_d2"></td>
            <td><input type="text" id="pjz2_d3" name="pjz2_d3"></td>
            <td><input type="text" id="pjz2_d4" name="pjz2_d4"></td>
            <td><input type="text" id="pjz2_d5" name="pjz2_d5"></td>
            <td><input type="text" id="pjz2_d6" name="pjz2_d6"></td>
            <td><input type="text" id="pjz2_d7" name="pjz2_d7"></td>
            <td><input type="text" id="pjz2_d8" name="pjz2_d8"></td>
          </tr>
          <tr>
            <td colspan="2">平均贯入力F=F1-F0（N）</td>
            <td><input type="text" id="pjgr2_d1" name="pjgr2_d1"></td>
            <td><input type="text" id="pjgr2_d2" name="pjgr2_d2"></td>
            <td><input type="text" id="pjgr2_d3" name="pjgr2_d3"></td>
            <td><input type="text" id="pjgr2_d4" name="pjgr2_d4"></td>
            <td><input type="text" id="pjgr2_d5" name="pjgr2_d5"></td>
            <td><input type="text" id="pjgr2_d6" name="pjgr2_d6"></td>
            <td><input type="text" id="pjgr2_d7" name="pjgr2_d7"></td>
            <td><input type="text" id="pjgr2_d8" name="pjgr2_d8"></td>
          </tr>
          <tr>
            <td colspan="2">P=F/A（MPa）</td>
            <td><input type="text" id="p2_d1" name="p2_d1"></td>
            <td><input type="text" id="p2_d2" name="p2_d2"></td>
            <td><input type="text" id="p2_d3" name="p2_d3"></td>
            <td><input type="text" id="p2_d4" name="p2_d4"></td>
            <td><input type="text" id="p2_d5" name="p2_d5"></td>
            <td><input type="text" id="p2_d6" name="p2_d6"></td>
            <td><input type="text" id="p2_d7" name="p2_d7"></td>
            <td><input type="text" id="p2_d8" name="p2_d8"></td>
          </tr>
          <tr>
            <td rowspan="8" style="width: 80px;">
              试样（3）<br/>F0=
              <input type="text" name="f0sy3" id="f0sy3" style="width: 40px; height: 23px;"><br/>
              （N）
            </td>
            <td colspan="2">时间（h）</td>
            <td><input type="text" id="sj3_d1" name="sj3_d1"></td>
            <td><input type="text" id="sj3_d2" name="sj3_d2"></td>
            <td><input type="text" id="sj3_d3" name="sj3_d3"></td>
            <td><input type="text" id="sj3_d4" name="sj3_d4"></td>
            <td><input type="text" id="sj3_d5" name="sj3_d5"></td>
            <td><input type="text" id="sj3_d6" name="sj3_d6"></td>
            <td><input type="text" id="sj3_d7" name="sj3_d7"></td>
            <td><input type="text" id="sj3_d8" name="sj3_d8"></td>
          </tr>
          <tr>
            <td colspan="2">气温（℃）</td>
            <td><input type="text" id="qw3_d1" name="qw3_d1"></td>
            <td><input type="text" id="qw3_d2" name="qw3_d2"></td>
            <td><input type="text" id="qw3_d3" name="qw3_d3"></td>
            <td><input type="text" id="qw3_d4" name="qw3_d4"></td>
            <td><input type="text" id="qw3_d5" name="qw3_d5"></td>
            <td><input type="text" id="qw3_d6" name="qw3_d6"></td>
            <td><input type="text" id="qw3_d7" name="qw3_d7"></td>
            <td><input type="text" id="qw3_d8" name="qw3_d8"></td>
          </tr>
          <tr>
            <td colspan="2">A（mm2）</td>
            <td><input type="text" id="a3_d1" name="a3_d1"></td>
            <td><input type="text" id="a3_d2" name="a3_d2"></td>
            <td><input type="text" id="a3_d3" name="a3_d3"></td>
            <td><input type="text" id="a3_d4" name="a3_d4"></td>
            <td><input type="text" id="a3_d5" name="a3_d5"></td>
            <td><input type="text" id="a3_d6" name="a3_d6"></td>
            <td><input type="text" id="a3_d7" name="a3_d7"></td>
            <td><input type="text" id="a3_d8" name="a3_d8"></td>
          </tr>
          <tr>
            <td rowspan="3">读数F1（N）</td>
            <td>点1</td>
            <td><input type="text" id="ds3_d11" name="ds3_d11"></td>
            <td><input type="text" id="ds3_d21" name="ds3_d21"></td>
            <td><input type="text" id="ds3_d31" name="ds3_d31"></td>
            <td><input type="text" id="ds3_d41" name="ds3_d41"></td>
            <td><input type="text" id="ds3_d51" name="ds3_d51"></td>
            <td><input type="text" id="ds3_d61" name="ds3_d61"></td>
            <td><input type="text" id="ds3_d71" name="ds3_d71"></td>
            <td><input type="text" id="ds3_d81" name="ds3_d81"></td>
          </tr>
          <tr>
            <td>点2</td>
            <td><input type="text" id="ds3_d12" name="ds3_d12"></td>
            <td><input type="text" id="ds3_d22" name="ds3_d22"></td>
            <td><input type="text" id="ds3_d32" name="ds3_d32"></td>
            <td><input type="text" id="ds3_d42" name="ds3_d42"></td>
            <td><input type="text" id="ds3_d52" name="ds3_d52"></td>
            <td><input type="text" id="ds3_d62" name="ds3_d62"></td>
            <td><input type="text" id="ds3_d72" name="ds3_d72"></td>
            <td><input type="text" id="ds3_d82" name="ds3_d82"></td>
          </tr>
          <tr>
            <td>平均值</td>
            <td><input type="text" id="pjz3_d1" name="pjz3_d1"></td>
            <td><input type="text" id="pjz3_d2" name="pjz3_d2"></td>
            <td><input type="text" id="pjz3_d3" name="pjz3_d3"></td>
            <td><input type="text" id="pjz3_d4" name="pjz3_d4"></td>
            <td><input type="text" id="pjz3_d5" name="pjz3_d5"></td>
            <td><input type="text" id="pjz3_d6" name="pjz3_d6"></td>
            <td><input type="text" id="pjz3_d7" name="pjz3_d7"></td>
            <td><input type="text" id="pjz3_d8" name="pjz3_d8"></td>
          </tr>
          <tr>
            <td colspan="2">平均贯入力F=F1-F0（N）</td>
            <td><input type="text" id="pjgr3_d1" name="pjgr3_d1"></td>
            <td><input type="text" id="pjgr3_d2" name="pjgr3_d2"></td>
            <td><input type="text" id="pjgr3_d3" name="pjgr3_d3"></td>
            <td><input type="text" id="pjgr3_d4" name="pjgr3_d4"></td>
            <td><input type="text" id="pjgr3_d5" name="pjgr3_d5"></td>
            <td><input type="text" id="pjgr3_d6" name="pjgr3_d6"></td>
            <td><input type="text" id="pjgr3_d7" name="pjgr3_d7"></td>
            <td><input type="text" id="pjgr3_d8" name="pjgr3_d8"></td>
          </tr>
          <tr>
            <td colspan="2">P=F/A（MPa）</td>
            <td><input type="text" id="p3_d1" name="p3_d1"></td>
            <td><input type="text" id="p3_d2" name="p3_d2"></td>
            <td><input type="text" id="p3_d3" name="p3_d3"></td>
            <td><input type="text" id="p3_d4" name="p3_d4"></td>
            <td><input type="text" id="p3_d5" name="p3_d5"></td>
            <td><input type="text" id="p3_d6" name="p3_d6"></td>
            <td><input type="text" id="p3_d7" name="p3_d7"></td>
            <td><input type="text" id="p3_d8" name="p3_d8"></td>
          </tr>
        </table>
      </div>
      <div class="tableRemark"></div>
    </form>
  </div>

</template>

<script>
import {getAction, postAction} from "@api/manage";

export default {
  name: 'JJ0507',
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
