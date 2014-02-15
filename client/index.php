<html>

<?php
	include 'vars.php';
?>

<form action="https://api.venmo.com/v1/payments" method="post">
	Phone: <input type="text" name="phone"><br />
	note: <input type="text" name="note"><br />
	amount: <input type="text" name="amount"><br />
	<input type="hidden" name="access_token" value="<?php echo $access_token ?>" />
	<input type ="submit" value="Submit" />
</form> 
</html>