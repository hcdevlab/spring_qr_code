
let LOGIN_TASKS = {};

LOGIN_TASKS.login = function ()
{
	let userLogin = {};

	userLogin.username = document.querySelector('#username_login').value;
	userLogin.password = document.querySelector('#password_login').value;

	let stringParam;

	if ((userLogin.username === '') || (userLogin.password === ''))
	{
		alert('Complete todos los datos.');
		document.querySelector('#username_login').value = '';
		document.querySelector('#password_login').value = '';
		document.querySelector('#username_login').focus();
		return false;
	}
	else
	{
		stringParam = JSON.stringify(userLogin);

		let tokenName = document.querySelector('#crossToken').getAttribute('name');
		let tokenValue = document.querySelector('#crossToken').getAttribute('value');

		let headers = new Headers();
		headers.append(tokenName, tokenValue);

		let setRequestParam = {
			credentials: 'include',
			method     : 'POST',
			headers    : headers,
			body       : stringParam
		};

		let fetchRequest = new Request('/secpath', setRequestParam);

		const tryLogin = async () =>
		{
			let fetchResponse = await fetch(fetchRequest);
			if (fetchResponse.status === 200)
			{
				window.location = '/private/data_table';
			}
			else
			{
				console.log("Recibido 1: ", fetchResponse.headers);
				console.log("Recibido 2: ", fetchResponse.status);
				console.log("Recibido 3: ", fetchResponse.statusText);
				document.querySelector('#username_login').value = '';
				document.querySelector('#password_login').value = '';
				document.querySelector('#username_login').focus();
				alert('Los datos ingresados no corresponden a un usuario registrado.');
			}
		};
		tryLogin().catch(function (error)
		{
			console.log('Hubo un problema con la petición Fetch en tryLogin: ' + error.message);
		});
	}
};

LOGIN_TASKS.initialize = function ()
{
	document.querySelector('#username_login').focus();
	document.querySelector('#login_button').setAttribute('onclick', 'LOGIN_TASKS.login()');
};

LOGIN_TASKS.initialize();
