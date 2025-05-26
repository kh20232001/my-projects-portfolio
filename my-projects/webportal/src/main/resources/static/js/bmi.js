/**
 * 入力チェックをするjs
 */
function check(){
	// 入力値を取得する。
	const height = document.getElementById('height').value;
	const weight = document.getElementById('weight').value;
	// 数値に変換できるかチェック
	if(isNaN(height) || isNaN(weight)){
		alert("数値ではない文字が入力されました。")	
		return false
	}
	// 入力値を小数に変換する
	const Fheight = parseFloat(height)
	const Fweight = parseFloat(weight)
	// 入力値が範囲内かどうかチェックする
	if(!(Fheight >= 30.0 && Fheight <= 250.0) || (!(Fweight >= 5.0 && Fweight <= 200.0))){
		alert("範囲外の数値が入力されました。")
		return false
	}
	
	return true
	
}