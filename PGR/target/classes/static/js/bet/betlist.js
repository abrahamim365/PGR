window.onload = function(){

const containerElem = document.querySelector('.bet_container')
if(containerElem) {
	fetch('/betroomlist') //list에 넣을 내용 불러옴
	.then(res => res.json())
	.then(myJson => {
		createTable(myJson)
	})
	
	function createTable(myJson) {
		//4일 내에 경기가 없을 경우 표시
		if (myJson.length === 0) {
			const list = document.createElement('div')
			list.innerText = '4일 내에 예정된 경기가 없습니다.'
			list.classList.add('setgame')
			containerElem.append(list)
			return
		}
		//list를 불러왔기 때문에 for문 돌림
		for(var i=0;i<myJson.length;i++) {
			var ahref = document.createElement('a')
			var div = document.createElement('div')
			var lteamlogo = document.createElement('div')
			var lteam = document.createElement('div')
			var rteamlogo = document.createElement('div')
			var rteam = document.createElement('div')
			var textspan = document.createElement('span')
			var datespan = document.createElement('span')
			var statuspan = document.createElement('span')
			var dates = new Date(myJson[i].date)
			var span = document.createElement('div')
			
			var limg = document.createElement('img')
			var rimg = document.createElement('img')
			var lString = 'img/teamlogo/' + myJson[i].lid + '.png'
			var rString = 'img/teamlogo/' + myJson[i].rid + '.png'
			
			limg.src = lString
			rimg.src = rString
			
			ahref.href = `/bettingroom?id=${myJson[i].id}`
			div.className = 'box'
			lteam.className = 'lTeam'
			lteamlogo.className = 'lTeamlogo'
			rteam.className = 'rTeam'
			rteamlogo.className = 'rTeamlogo'
			limg.className = 'teamlogo'
			rimg.className = 'teamlogo'
			span.className = 'CenterSpan'
			textspan.className = 'text'
			
			datespan.innerText = dates.toLocaleString()
			textspan.innerText = 'VS'
			statuspan.className = new Date() > dates ? 'play' : 'expected'
			lteam.innerText = myJson[i].lteam
			rteam.innerText = myJson[i].rteam
			
			
			lteamlogo.append(limg)
			rteamlogo.append(rimg)
			span.append(datespan)
			span.append(statuspan)
			span.append(textspan)
			
			
			div.append(ahref)
			ahref.append(lteamlogo)
			ahref.append(lteam)
			ahref.append(span)
			ahref.append(rteamlogo)
			ahref.append(rteam)
			containerElem.append(div)
		}
	}
}
}
