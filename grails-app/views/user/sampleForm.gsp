<html>

	<head>
		<meta name="layout" content="main"/>
		<title>css selectors</title>
		<style type="text/css" media="screen">
			* {
				font-family: sans-serif;
				color: white;
			}
			
			h1, form h2 {
				background: gray;
			}
			
			h1, h2, form {
				margin: 0;
			}
			
			h1 {
				font-size: 24px;
				padding: 5px;
				border-bottom: 5px solid black;
				letter-spacing: -2px;
				width: 510px;
				/* -moz-border-radius-topleft: 10px;
				-moz-border-radius-topright: 10px;*/
				border-radius-topleft: 10px;
				border-radius-topright: 10px;
			}
			
			form {
				background: lightgrey;
				padding: 10px;
				width: 500px;
				/* -moz-border-radius-bottomleft: 10px;
				-moz-border-radius-bottomright: 10px; */
				border-radius-bottomleft: 10px;
				border-radius-bottomright: 10px;
			}
			
			form label {
				display: block;
				font-weight: bold;
				margin: 5px;
				width: 225px;
				text-align: right;
				background: black;
			}
			
			form h2, form label {
				font-size: 15px;
				/* -moz-border-radius: 10px; */
				border-radius: 10px;
				padding: 3px;
			}
			
			/* new rules for this example */
			form > div > label {
				float: left;
			}
			
			form > div {
				clear: left;
			}
			
			div > input, div > select {
				margin: 3px;
				padding: 4px;
				color: #FF4500;
			}
			
			select > option {
				padding: 4px;
				color: #FF4500;	
			}
			
			label + input, label + select, label + textarea {
				background: darkgrey;
				color: #FF4500;
				border: 0;
			}
								
			option[value='newspaper'] {
				background: red;
			}						
								
			option[value='radio'] {
				background: blue;
			}				
			
			option[value='television'] {
				background: green;
			}	
								
			option[value='magazine'] {
				background: yellow;
			}					
								
			option[value='other'] {
				background: lavender;
			}					
								
		</style>			
	</head>
	
	<body>
		<h1>Widgets and What's-its</h1>
		<form>
			<h2>Tell us what's on your mind...</h2>
			
			<div>
				<label for="feedback[name]">Name: </label>
				<input type="text" size="25" name="feedback[name]" />
			</div>
			
			<div>
				<label for="feedback[email]">Email: </label>
				<input type="text" size="25" name="feedback[email]" />
			</div>
			
			<div>
				<label for="feedback[message]">Comments: </label>
				<textarea name="feedback[message]" cols="40" rows="3" ></textarea>
			</div>			
			
			<div>
				<label for="feedback[address]">Address:</label>
				<textarea name="feedback[address]" cols="40" rows="3" ></textarea>
			</div>
			
			<div>
				<label for="feedback[heard]">How'd you hear about us?</label>
				<select name="feedback[heard]">
					<option value="foo">Choose...</option>
					<option value="newspaper">Newspaper</option>
					<option value="magazine">Magazine</option>
					<option value="television">Television</option>
					<option value="radio">Radio</option>
					<option value="other">Other</option>	
				</select>
			</div>
			
		</form>
	</body>
	
</html>