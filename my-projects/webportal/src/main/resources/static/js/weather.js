/**
 * 入力チェックをするjs
 */
function check(){
	// 入力値を取得する。
	const citycode = document.getElementById('citycode').value;
	// 数値に変換できるかチェック
	if(isNaN(citycode)){
		alert("数値ではない文字が入力されました。")	
		return false
	}
	if(citycode.length != 6){
		alert("6桁で入力してください")
		return false
	}
	return true
}