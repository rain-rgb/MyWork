// uniapp在H5中各API的z-index值如下：
/**
 * actionsheet: 999
 * modal: 999
 * navigate: 998
 * tabbar: 998
 * toast: 999
 */

export default {
    toast: 690,
    noNetwork: 680,
    // popup包含popup，actionsheet，keyboard，picker的值
    popup: 675,
    mask: 670,
    navbar: 580,
    topTips: 575,
    sticky: 575,
    indexListSticky: 565
}
