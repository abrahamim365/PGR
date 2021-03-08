const temps = document.querySelector('.playcontainer')
const idvalue = temps.getAttribute('value')

if(temps) {
	fetch('/record?id=' + idvalue)
	.then(res => res.json())
	.then(myJson => {
		createText(myJson)
	})
	temps.removeAttribute('value')
	
	function createText(myJson) {
		const leftinfo = document.querySelector('.left-information')
		const rightinfo = document.querySelector('.right-information')
		
		const ul = document.createElement('ul')
		const li1 = document.createElement('li')
		const li2 = document.createElement('li')
		const li3 = document.createElement('li')
		const li4 = document.createElement('li')
		const li5 = document.createElement('li')
		
		const ul2 = document.createElement('ul')
		const li12 = document.createElement('li')
		const li22 = document.createElement('li')
		const li32 = document.createElement('li')
		const li42 = document.createElement('li')
		const li52 = document.createElement('li')
		
		li1.innerText = '슈팅 : ' + myJson.ltotalShots
		li2.innerText = '점유율 : ' + myJson.lpossessionPct + '%'
		li3.innerText = '유효슈팅 : ' + myJson.lshotsOnTarget
		li4.innerText = '파울 : ' + myJson.lfoulsCommitted
		li5.innerText = '코너킥 : ' + myJson.lwonCorners
		
		li12.innerText = '슈팅 : ' + myJson.rtotalShots
		li22.innerText = '점유율 : ' + myJson.rpossessionPct + '%'
		li32.innerText = '유효슈팅 : ' + myJson.rshotsOnTarget
		li42.innerText = '파울 : ' + myJson.rfoulsCommitted
		li52.innerText = '코너킥 : ' + myJson.rwonCorners
		
		ul.append(li1)
		ul.append(li3)
		ul.append(li2)
		ul.append(li4)
		ul.append(li5)
		
		leftinfo.append(ul)
		
		ul2.append(li12)
		ul2.append(li32)
		ul2.append(li22)
		ul2.append(li42)
		ul2.append(li52)
		
		rightinfo.append(ul2)
	}
}

const container = document.querySelector('.container')
const betcontainer = document.querySelector('.betcontainer')
if(container) {
	fetch('/recentMatch?id=' + idvalue)
	.then(res => res.json())
	.then(myJson => {
		createText(myJson)
	})
	
	function createText(myJson) {
		const leftinfo = document.querySelector('.left-information')
		const rightinfo = document.querySelector('.right-information')
		const lteam = document.querySelector('.leftContainer')
		const rteam = document.querySelector('.rightContainer')
		const lscore = document.querySelector('.lscore')
		const rscore = document.querySelector('.rscore')
		const date = document.querySelector('.date')
		const venue = document.querySelector('.stadium')
		const dates = new Date(myJson.date)
		
		const lspan = document.createElement('span')
		const rspan = document.createElement('span')
		var limg = document.createElement('img')
		var rimg = document.createElement('img')
		
		var lString = 'img/teamlogo/' + myJson.lid + '.png'
		var rString = 'img/teamlogo/' + myJson.rid + '.png'

		limg.src = lString
		rimg.src = rString
		
		if(myJson.lscore > myJson.rscore) {
      		lteam.className += 'color'
			leftinfo.className += 'color'
		} else if(myJson.lscore < myJson.rscore) {
			rteam.className += 'color'
			rightinfo.className += 'color'
		} else if(myJson.lscore === myJson.rscore) {
      		lteam.className += 'color'
			leftinfo.className += 'color'
			rteam.className += 'color'
			rightinfo.className += 'color'
		}
		
		lteam.append(lspan)
		rteam.append(rspan)
		lspan.innerText = myJson.lteam
		rspan.innerText = myJson.rteam
		lteam.append(limg)
		rteam.append(rimg)
		lscore.innerText = myJson.lscore
		rscore.innerText = myJson.rscore
		date.innerText = dates.toLocaleString()
		venue.innerText = myJson.venue
	}
}

if(betcontainer) {
	
	let myJson2 = null
	fetch('/betUser?id=' + idvalue + '&userPk=' + )
	.then(res => res.json())
	.then(myJson => {
		myJson2 = myJson
		console.log(myJson2)
	})
	
	fetch('/betallocation?id=' + idvalue)
	.then(res => res.json())
	.then(myJson => {
		createAllocation(myJson)
		console.log(myJson)
	})
	
	function createAllocation(myJson) {
		const betgraph = document.querySelector('.betgraph')
		const allocation = document.querySelector('.allocation')
		const horizontalbar_w = document.createElement('div')
		const horizontalbar_d = document.createElement('div')
		const horizontalbar_l = document.createElement('div')
		
		//myJson2.
		//document.getElementsByclassName("share_1").style.width = "";
		
		horizontalbar_w.innerText  = '승 : x'+ myJson.w_allocation.toFixed(2)
		horizontalbar_d.innerText  = '무 : x' + myJson.d_allocation.toFixed(2)
		horizontalbar_l.innerText  = '패 : x' + myJson.l_allocation.toFixed(2)
		
		horizontalbar_w.className = 'horizontalbar_w'
		horizontalbar_d.className = 'horizontalbar_d'		
		horizontalbar_l.className = 'horizontalbar_l'
		
		allocation.append(horizontalbar_w)
		allocation.append(horizontalbar_d)
		allocation.append(horizontalbar_l)
	}
}