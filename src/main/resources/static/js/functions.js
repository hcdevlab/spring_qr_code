
let TASKS = {};

TASKS.chargeData = function ()
{
	let tokenName = document.querySelector('#crossToken').getAttribute('name');
	let tokenValue = document.querySelector('#crossToken').getAttribute('value');

	let headers = new Headers();
	headers.append(tokenName, tokenValue);

	let setRequestParam = {
		credentials: 'include',
		method     : 'POST',
		headers    : headers
	};

	let fetchRequest = new Request('/private/init_table', setRequestParam);

	const displayTable = async () =>
	{
		let fetchResponse = await fetch(fetchRequest);
		if (fetchResponse.status === 200)
		{
			let content = await fetchResponse.arrayBuffer();
			let decodedContent = new TextDecoder('utf-8').decode(content);
			let destiny = document.querySelector('#block_content');
			destiny.innerHTML = decodedContent;
			TASKS.setMapFunctionToButtonLoop();
		}
		else
		{
			alert('Problemas de conexión.');
		}
	};
	displayTable().catch(function (error)
	{
		console.log(error.message);
	});
};

TASKS.openPopUpMap = function (param)
{
	TASKS.setGoogleMapCoords(param);

	document.querySelector('#popup-container-map').setAttribute('style', 'display:block;');

	if (screen.width >= 768)
	{
		document.querySelector('#modal-map').setAttribute('style', 'width:70%;');
	}
	else
	{
		document.querySelector('#modal-map').setAttribute('style', 'width:95%;');
	}

	let closePopUp = document.querySelector('#close_button_update');
	let cancelPopUp = document.querySelector('#cancel_button_update');

	closePopUp.addEventListener('click', (e) =>
	{
		let div_here_map = document.querySelector('#map-container');
		if (div_here_map.children[0])
		{
			div_here_map.removeChild(div_here_map.children[0]);
		}
		document.querySelector('#popup-container-map').style.display = 'none';
	});

	cancelPopUp.addEventListener('click', (e) =>
	{
		let div_here_map = document.querySelector('#map-container');
		if (div_here_map.children[0])
		{
			div_here_map.removeChild(div_here_map.children[0]);
		}
		document.querySelector('#popup-container-map').style.display = 'none';
	});
};

TASKS.setMapFunctionToButtonLoop = function ()
{
	let all_map_buttons = document.querySelectorAll('.onclick_map');

	for (let i = 0; i < all_map_buttons.length; i++)
	{
		let single_map_button = all_map_buttons[i];
		single_map_button.setAttribute('onclick', 'TASKS.openPopUpMap(this)');
	}
};

TASKS.setGoogleMapCoords = function (param)
{
	let latCoord = param.parentNode.parentNode.parentNode.children[4].innerHTML;
	let longCoord = param.parentNode.parentNode.parentNode.children[5].innerHTML;
	let coords = new google.maps.LatLng(latCoord, longCoord);
	let mapOptions = {
		center        : coords,
		zoom          : 18,
		mapTypeControl: true,
		mapTypeId     : google.maps.MapTypeId.ROAD
	};
	let map = new google.maps.Map(document.querySelector('#map-container'), mapOptions);

	let marker = new google.maps.Marker({
		position: coords,
		map     : map,
		title   : 'Ubicación'
	});
};

TASKS.deleteRecords = function ()
{
	let setRequestParam = {
		credentials: 'include',
		method     : 'GET'
	};

	let request = new Request('/private/delete_table', setRequestParam);

	fetch(request).then(function (response)
	{
		if (response.ok)
		{
			document.querySelector('#popup-ask').style.display = 'none';
			alert('Datos eliminados');
		}
		else
		{
			document.querySelector('#popup-ask').style.display = 'none';
			alert('Problemas en la eliminación de datos');
		}
	}).catch(function (error)
	{
		console.log(error.message);
	});
};

TASKS.setFunctionDelete = function ()
{
	let buttonDelete = document.querySelector('#button-delete');
	buttonDelete.setAttribute('onclick', 'TASKS.openPopUpDelete()');
};

TASKS.openPopUpDelete = function ()
{
	document.querySelector('#popup-ask').setAttribute('style', 'display:block;');
	let deleteConfirm = document.querySelector('#button_delete_yes');
	let deleteClose = document.querySelector('#button_delete_close');
	let deleteCancel = document.querySelector('#button_delete_cancel');
	deleteConfirm.setAttribute('onClick', 'TASKS.deleteRecords()');
	deleteClose.setAttribute('onClick', 'document.querySelector("#popup-ask").style.display = "none"');
	deleteCancel.setAttribute('onClick', 'document.querySelector("#popup-ask").style.display = "none"');
};

TASKS.paginate = function ()
{
	let tokenName = document.querySelector('#crossToken').getAttribute('name');
	let tokenValue = document.querySelector('#crossToken').getAttribute('value');

	let headers = new Headers();
	headers.append(tokenName, tokenValue);

	let setRequestData = {
		credentials: 'include',
		method     : 'POST',
		headers    : headers
	};

	let fetchRequest = new Request('/private/rows_number', setRequestData);

	const assignRowsNumber = async () =>
	{
		let fetchResponse = await(fetch(fetchRequest));
		let number = JSON.parse(await(fetchResponse.text()));
		TASKS.setPagination(parseInt(number), 20);
	};
	assignRowsNumber().catch(function (error)
	{
		console.log(error.message);
	});
};

TASKS.setPagination = function (paramTotalRows, paramLimit)
{
	let numberOfRows = parseInt(paramTotalRows);
	let numberLimit = parseInt(paramLimit);
	let numberOfPages = Math.trunc(numberOfRows / numberLimit);
	let numberOfButtons;

	if (screen.width >= 768)
	{
		numberOfButtons = 7;
	}
	else
	{
		numberOfButtons = 3;
	}

	if ((numberOfRows % numberLimit) !== 0)
	{
		numberOfPages = parseInt(numberOfPages + 1);
		let settingConfigRegular = {
			'totalRows' : numberOfRows,
			'totalPages': numberOfPages,
			'limit'     : numberLimit,
			'show'      : parseInt(numberOfButtons),
			'render'    : document.getElementById('pagination-bar')
		};

		TASKS.setPaginationBar(1, settingConfigRegular);
	}
	else
	{
		let settingConfigIrregular = {
			'totalRows' : numberOfRows,
			'totalPages': numberOfPages,
			'limit'     : numberLimit,
			'show'      : parseInt(numberOfButtons),
			'render'    : document.getElementById('pagination-bar')
		};

		TASKS.setPaginationBar(1, settingConfigIrregular);
	}
};

TASKS.setButtonFunction = function (paramButton, paramPage, paramSettingConfig)
{
	let first = paramButton.getAttribute('data-record-first');
	let last = paramButton.getAttribute('data-record-size');

	let tokenName = document.querySelector('#crossToken').getAttribute('name');
	let tokenValue = document.querySelector('#crossToken').getAttribute('value');

	let headers = new Headers();
	headers.append('Content-Type', 'application/json; charset=UTF-8');
	headers.append(tokenName, tokenValue);

	let page = {};
	page.firstParam = first.toString();
	page.lastParam = last.toString();
	let stringParam = JSON.stringify(page);

	let setRequestParam = {
		credentials: 'include',
		method     : 'POST',
		headers    : headers,
		body       : stringParam
	};

	let fetchRequest = new Request('/private/part_post', setRequestParam);

	const displayTable = async () =>
	{
		let fetchResponse = await fetch(fetchRequest);
		if (fetchResponse.status === 200)
		{
			let content = await fetchResponse.arrayBuffer();
			let decodedContent = new TextDecoder('utf-8').decode(content);
			let destiny = document.querySelector('#block_content');
			destiny.innerHTML = decodedContent;
			TASKS.setPaginationBar(paramPage, paramSettingConfig);
			TASKS.setMapFunctionToButtonLoop();
		}
		else
		{
			alert('Problemas de conexión.');
		}
	};
	displayTable().catch(function (error)
	{
		console.log(error.message);
	});
};

TASKS.setPaginationBar = function (p, settingConfig)
{
	p = parseInt(p);
	if (settingConfig.totalPages < settingConfig.show)
	{
		settingConfig.show = settingConfig.totalPages;
	}
	if (p > settingConfig.totalPages)
	{
		p = settingConfig.totalPages;
	}
	if (p < 1)
	{
		p = 1;
	}

	let balanceBefore = parseInt(Math.floor(settingConfig.show / 2));
	let balanceAfter = balanceBefore;
	if (settingConfig.show % 2 === 0)
	{
		balanceAfter--;
	}
	settingConfig.from = parseInt(p - balanceAfter);
	settingConfig.to = parseInt(p + balanceBefore);
	while (settingConfig.from < 1)
	{
		settingConfig.from++;
	}
	while (settingConfig.to > settingConfig.totalPages)
	{
		settingConfig.to--;
	}

	let pivot = (p - settingConfig.from);
	while (pivot < balanceBefore && settingConfig.to < settingConfig.totalPages)
	{
		settingConfig.to++;
		pivot++;
	}

	pivot = (settingConfig.to - p);
	if (settingConfig.show % 2 === 0)
	{
		while (pivot <= balanceAfter && settingConfig.from > 1)
		{
			settingConfig.from--;
			pivot++;
		}
	}
	else
	{
		while (pivot < balanceAfter && settingConfig.from > 1)
		{
			settingConfig.from--;
			pivot++;
		}
	}

	let valuePrevious = ((p - 1) < 1) ? 1 : (p - 1);
	let valueNext = ((p + 1) >= settingConfig.totalPages) ? settingConfig.totalPages : (p + 1);

	let tableTr = document.querySelector('#table-foot');
	let tableFooter = document.createElement('td');
	tableFooter.setAttribute('colspan', '7');
	tableFooter.innerHTML = 'P' + '\u00e1' + 'gina ' + p.toString() + ' de ' + settingConfig.totalPages.toString();

	let div = document.createElement('div');
	div.setAttribute('class', 'pagination-bar');

	let buttonFirst = document.createElement('button');
	buttonFirst.innerHTML = 'primera';
	buttonFirst.style.width = '75px';
	buttonFirst.setAttribute('data-page-number', 1);
	buttonFirst.setAttribute('data-record-first', 0);
	buttonFirst.setAttribute('data-record-size', settingConfig.limit);
	if (p === 1)
	{
		buttonFirst.setAttribute('class', 'page-button page-disable');
	}
	else
	{
		buttonFirst.setAttribute('class', 'page-button');
		buttonFirst.addEventListener('click', (e) =>
		{
			let page = e.target.dataset.pageNumber;
			TASKS.setButtonFunction(buttonFirst, page, settingConfig);
		});
	}
	div.appendChild(buttonFirst);

	let buttonPrevious = document.createElement('button');
	buttonPrevious.innerHTML = '\u2b05';
	let offsetPrevious = parseInt(parseInt(valuePrevious - 1) * settingConfig.limit);
	buttonPrevious.setAttribute('data-page-number', valuePrevious);
	buttonPrevious.setAttribute('data-record-first', offsetPrevious);
	buttonPrevious.setAttribute('data-record-size', settingConfig.limit);

	if (p === valuePrevious)
	{
		buttonPrevious.setAttribute('class', 'page-button page-disable');
	}
	else
	{
		buttonPrevious.setAttribute('class', 'page-button');
		buttonPrevious.addEventListener('click', (e) =>
		{
			let page = e.target.dataset.pageNumber;
			TASKS.setButtonFunction(buttonPrevious, page, settingConfig);
		});
	}
	div.appendChild(buttonPrevious);

	for (let i = settingConfig.from; i <= settingConfig.to; i++)
	{
		let offsetLoop = parseInt(parseInt(i - 1) * settingConfig.limit);

		let limit = settingConfig.limit;

		if (parseInt(settingConfig.totalRows % settingConfig.limit) !== 0)
		{
			if (i === settingConfig.totalPages)
			{
				limit = parseInt(settingConfig.totalRows % limit);
			}
		}

		let buttonLoop = document.createElement('button');
		buttonLoop.innerHTML = i;
		buttonLoop.setAttribute('data-page-number', i);
		buttonLoop.setAttribute('data-record-first', offsetLoop);
		buttonLoop.setAttribute('data-record-size', limit);

		if (i === p)
		{
			buttonLoop.setAttribute('class', 'page-button active-page');
		}
		else
		{
			buttonLoop.setAttribute('class', 'page-button');
			buttonLoop.addEventListener('click', (e) =>
			{
				let page = e.target.dataset.pageNumber;
				TASKS.setButtonFunction(buttonLoop, page, settingConfig);
			});
		}
		div.appendChild(buttonLoop);
	}

	let buttonNext = document.createElement('button');
	buttonNext.innerHTML = '\u27a1';
	let offsetNext = parseInt(parseInt(valueNext - 1) * settingConfig.limit);
	let limitNext = settingConfig.limit;
	if (parseInt(settingConfig.totalRows % settingConfig.limit) !== 0)
	{
		if (valueNext === settingConfig.totalPages)
		{
			limitNext = parseInt(settingConfig.totalRows % limitNext);
		}
	}

	buttonNext.setAttribute('data-page-number', valueNext);
	buttonNext.setAttribute('data-record-first', offsetNext);
	buttonNext.setAttribute('data-record-size', limitNext);

	if (p === valueNext)
	{
		buttonNext.setAttribute('class', 'page-button page-disable');
	}
	else
	{
		buttonNext.setAttribute('class', 'page-button');
		buttonNext.addEventListener('click', (e) =>
		{
			let page = e.target.getAttribute('data-page-number');
			TASKS.setButtonFunction(buttonNext, page, settingConfig);
		});
	}
	div.appendChild(buttonNext);

	let buttonLast = document.createElement('button');
	buttonLast.innerHTML = '\xFA' + 'ltima';
	buttonLast.style.width = '75px';
	let offsetLast = parseInt(settingConfig.totalPages - 1) * 20;
	let limitLast = settingConfig.limit;

	if (parseInt(settingConfig.totalRows % settingConfig.limit) !== 0)
	{
		limitLast = parseInt(settingConfig.totalRows % limitLast);
	}
	buttonLast.setAttribute('data-page-number', settingConfig.totalPages);
	buttonLast.setAttribute('data-record-first', offsetLast);
	buttonLast.setAttribute('data-record-size', (limitLast));
	if (p === settingConfig.totalPages)
	{
		buttonLast.setAttribute('class', 'page-button page-disable');
	}
	else
	{
		buttonLast.setAttribute('class', 'page-button');
		buttonLast.addEventListener('click', (e) =>
		{
			let page = e.target.dataset.pageNumber;
			TASKS.setButtonFunction(buttonLast, page, settingConfig);
		});
	}
	div.appendChild(buttonLast);

	tableTr.innerHTML = '';
	settingConfig.render.innerHTML = '';
	settingConfig.render.appendChild(div);
	tableTr.appendChild(tableFooter);
};

TASKS.exit = function ()
{
	let tokenName = document.querySelector('#crossToken').getAttribute('name');
	let tokenValue = document.querySelector('#crossToken').getAttribute('value');

	let headers = new Headers();
	headers.append(tokenName, tokenValue);

	let data = {
		credentials: 'include',
		method     : 'POST',
		headers    : headers
	};

	let request = new Request('/private/log_out', data);

	fetch(request).then(function (response)
	{
		if (response.ok)
		{
			window.location = '/index';
		}
		else
		{
			alert('Problemas.');
		}
	}).catch(function (error)
	{
		console.log(error.message);
	});
};

TASKS.initialize = function ()
{
	document.querySelector('#button-exit').setAttribute('onclick', 'TASKS.exit()');
	document.querySelector('#button-excel').setAttribute('href', '/private/downloadExcel');
	TASKS.chargeData();
	// TASKS.setFunctionDelete();
	TASKS.paginate();
};

TASKS.initialize();
