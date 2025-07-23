<template>
  <div>
    <form id="form" action="" method="post" onsubmit="return false;" class="">
      <!--引用公用表头部分 固定高度234px-->
      <div class="tableHeader" style="height: 234px;"></div>
      <!--表内容部分 固定高度 670px-->
      <div class="tableContent" style="height: 353px;">
        <table class="layui-table" style="border: 2px solid #000;box-sizing: border-box;">
          <tr>
            <td colspan="2">混凝土设计强度等级</td>
            <td><input type="text" name="hunntsjqddj" id="hunntsjqddj" /></td>
            <td>砼配合比编号</td>
            <td><input type="text" name="tongphbbh" id="tongphbbh" /></td>
            <td>拌和方式</td>
            <td><input type="text" name="banhfs" id="banhfs" /></td>
            <td>设计坍落度(mm)</td>
            <td><input type="text" name="shejtanld" id="shejtanld" /></td>
          </tr>
          <tr>
            <td rowspan="2" colspan="3">配合比</td>
            <td>砂</td>
            <td>石</td>
            <td>水泥</td>
            <td>外加剂</td>
            <td>掺合料</td>
            <td>水</td>
          </tr>
          <tr>
            <td><input type="text" name="peihbsha" id="peihbsha" /></td>
            <td><input type="text" name="peihbshi" id="peihbshi" /></td>
            <td><input type="text" name="peihbshuin" id="peihbshuin" /></td>
            <td><input type="text" name="peihbwaijj" id="peihbwaijj" /></td>
            <td><input type="text" name="peihbchanhl" id="peihbchanhl" /></td>
            <td><input type="text" name="peihbshui" id="peihbshui" /></td>
          </tr>
          <tr>
            <td width="4%" rowspan="7">坍落度仪法</td>
            <td rowspan="2">样品编号</td>
            <td colspan="3">坍落度(mm)</td>
            <td colspan="4">目测内容</td>
          </tr>
          <tr>
            <td>1</td>
            <td>2</td>
            <td>平均</td>
            <td>棍度</td>
            <td>含砂情况</td>
            <td>粘聚性</td>
            <td>保水性</td>
          </tr>
          <tr>
            <td width="10%"><input type="text" name="tanldyangpbh1" id="tanldyangpbh1" /></td>
            <td width="12%"><input type="text" name="tanld11" id="tanld11" onblur="js()"/></td>
            <td width="12%"><input type="text" name="tanld21" id="tanld21" onblur="js()"/></td>
            <td width="12%"><input type="text" name="tanldpingj1" id="tanldpingj1" readonly /></td>
            <td width="12%"><input type="text" name="gund1" id="gund1" /></td>
            <td width="12%"><input type="text" name="hansqk1" id="hansqk1" /></td>
            <td width="12%"><input type="text" name="nianjx1" id="nianjx1" /></td>
            <td width="12%"><input type="text" name="baosx1" id="baosx1" /></td>
          </tr>
          <tr>
            <td><input type="text" name="tanldyangpbh2" id="tanldyangpbh2" /></td>
            <td><input type="text" name="tanld12" id="tanld12" onblur="js()"/></td>
            <td><input type="text" name="tanld22" id="tanld22" onblur="js()"/></td>
            <td><input type="text" name="tanldpingj2" id="tanldpingj2" readonly /></td>
            <td><input type="text" name="gund2" id="gund2" /></td>
            <td><input type="text" name="hansqk2" id="hansqk2" /></td>
            <td><input type="text" name="nianjx2" id="nianjx2" /></td>
            <td><input type="text" name="baosx2" id="baosx2" /></td>
          </tr>
          <tr>
            <td><input type="text" name="tanldyangpbh3" id="tanldyangpbh3" /></td>
            <td><input type="text" name="tanld13" id="tanld13" onblur="js()"/></td>
            <td><input type="text" name="tanld23" id="tanld23" onblur="js()"/></td>
            <td><input type="text" name="tanldpingj3" id="tanldpingj3" readonly /></td>
            <td><input type="text" name="gund3" id="gund3" /></td>
            <td><input type="text" name="hansqk3" id="hansqk3" /></td>
            <td><input type="text" name="nianjx3" id="nianjx3" /></td>
            <td><input type="text" name="baosx3" id="baosx3" /></td>
          </tr>
          <tr>
            <td><input type="text" name="tanldyangpbh4" id="tanldyangpbh4" /></td>
            <td><input type="text" name="tanld14" id="tanld14" onblur="js()"/></td>
            <td><input type="text" name="tanld24" id="tanld24" onblur="js()"/></td>
            <td><input type="text" name="tanldpingj4" id="tanldpingj4" readonly /></td>
            <td><input type="text" name="gund4" id="gund4" /></td>
            <td><input type="text" name="hansqk4" id="hansqk4" /></td>
            <td><input type="text" name="nianjx4" id="nianjx4" /></td>
            <td><input type="text" name="baosx4" id="baosx4" /></td>
          </tr>
          <tr>
            <td><input type="text" name="tanldyangpbh5" id="tanldyangpbh5" /></td>
            <td><input type="text" name="tanld15" id="tanld15" onblur="js()"/></td>
            <td><input type="text" name="tanld25" id="tanld25" onblur="js()"/></td>
            <td><input type="text" name="tanldpingj5" id="tanldpingj5" readonly /></td>
            <td><input type="text" name="gund5" id="gund5" /></td>
            <td><input type="text" name="hansqk5" id="hansqk5" /></td>
            <td><input type="text" name="nianjx5" id="nianjx5" /></td>
            <td><input type="text" name="baosx5" id="baosx5" /></td>
          </tr>
        </table>
      </div>
      <!--表下部 固定高度140px-->
      <div class="tableRemark" style="height: 442px;"></div>
    </form>

  </div>

</template>

<script>
import {getAction, postAction} from "@api/manage";

export default {
  name: 'JJ0505',
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
