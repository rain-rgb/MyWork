 function base64Encode(input) {
  var rv
  rv = encodeURIComponent(input)
  rv = unescape(rv)
  rv = window.btoa(rv)
  return rv
}

 export {
   base64Encode
 }