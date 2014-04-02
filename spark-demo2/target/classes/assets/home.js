$(document).ready(function() {
	$("#test-button").click(function() {
		$.get("/clickme", function(data) {
			$("#response").text(data.message);
		});
	});
});