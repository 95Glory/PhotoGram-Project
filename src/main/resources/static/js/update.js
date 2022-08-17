// (1) 회원정보 수정
function update(userId, event) {
	event.preventDefault(); // 폼태그 액션을 막기!!
	let data = $("#profileUpdate").serialize(); // key=value
	
	console.log(data);
	
	$.ajax({
		type:"put",
		url:`/api/user/${userId}`,
		data:data,
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		dataType:"json"
	}).done(res=>{
		console.log("update 성공"); // HttpStatus 상태코드 200번대
		location.href = `/user/${userId}`;
	}).fail(error=>{ // HttpStatus 상태코드 200번대가 아닐 때
		console.log("update 실패"); // HttpStatus 상태코드 200번대
	});
}