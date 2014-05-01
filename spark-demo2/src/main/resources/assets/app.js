window.addEventListener('load', function(){
	//color vertical bars
	var colors = ["#f68e56", "#fff568", "#acd373", "#82ca9c", "#6dcff6", "#8393ca"];
	var i = 0;
	$(".vertical_hr").each(function(index){
		$(this).css("background-color", colors[i]);
		i = (i+1)%6
	});

	$(".attribute_li").each(function(index){
		$(this).mouseenter(function() {
			$(this).children(".quitbutton").css("display","block");
		}).mouseleave(function() {
			$(this).children(".quitbutton").css("display","none");
		});
	});

	$("#attributes-banner").mouseenter(function() {
		$("#attributes-banner").css("background-color", "#8f8f8f");
	}).mouseleave(function() {
		$("#attributes-banner").css("background-color", "#c4c4c4");
	});

	$("#attributes-banner").click(function() {
	    //click on
	    if($("#attributes-checkbox").css("display")=="none"){
	    	$("#attributes-checkbox").css("display", "block");
	    	$("#attributes-banner").css("background-color", "#8f8f8f");

	    }else{
	    	$("#attributes-checkbox").css("display", "none")
	    	$("#attributes-banner").css("background-color", "#c4c4c4");
	    }

	    //click off
	});


}, false);


function displayResponse(data){
	console.log(data);
}

function toggleDiv(mydiv)
{
	var divname = mydiv + "-div";
	var checkboxname = mydiv + " checkbox";
	var contentID = document.getElementById(divname);
	if (contentID.style.display == "none"){
		contentID.style.display = "block";
		document.getElementById(checkboxname).checked = true;
	}else
	{
		contentID.style.display = "none";
		var divform = mydiv+"-input";
		var divslider = mydiv+"-slider";
		document.getElementById(divform).value = "";
		document.getElementById(divslider).value = "";
		document.getElementById(checkboxname).checked = false;
	}
}