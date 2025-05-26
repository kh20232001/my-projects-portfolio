/**
 * 入力チェックをするjs
 */
function check(){
	// 入力値を取得する。
	const zipcode = document.getElementById('zipcode').value;
	// 数値に変換できるかチェック
	if(isNaN(zipcode)){
		alert("数値ではない文字が入力されました。")	
		return false
	}
	if(zipcode.length != 7){
		alert("7桁で入力してください")
		return false
	}
	return true
}