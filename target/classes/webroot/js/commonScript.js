
    
//var interval = d3.interval(update, 1000);

function updateHumidity(msg){
    
    time = Date.now();
    sData.push({'x': time, 'y': msg.vehicleHumidity});
    drawGraph(sData,'#humidityChart');
}


function drawGraph(graphData,id ){


    var line = d3.line()
	.curve(d3.curveBasis)
        .x(function(d) { return xScaleSpeed(d.x); })
        .y(function(d) { return yScaleSpeed(d.y); });

    var lineselection = svgSpeed.selectAll('.line_')
        .select('path');


    lineselection.interrupt()
    		.transition()
        .duration(duration)
        .ease(d3.easeLinear)
        //.attr('transform', 'translate(' + -(xScale.range()[0]/((duration / 100)-2)) + ',' + margin + ')');
        .attr('transform', 'translate(' + -(xScaleSpeed(graphData[graphData.length-1].x) -xScaleSpeed.range()[0]) + ',' + marginSpeed + ')');
    

    if (graphData[0].x < time - time_frame - duration ){
            console.log('shift');
            graphData.shift();
    }
		
    lineselection.attr('d',line)
        .attr('transform', 'translate(0,' + marginSpeed + ')');

    
    
    start_time = time - (duration * 2) - time_frame;

    xScaleSpeed.domain([time, time + (duration * 2) - time_frame])
        .range([sChartWidth - (marginSpeed *2),0]);

    d3.select(id)
        .select('svg')
        .select('g')
        .selectAll('.x.axis')
        .transition()
        .duration(duration)
        .ease(d3.easeLinear)
        .call(xAxis);
}

function inRange(x, min, max) {
    return ((x - min) * (x - max) <= 0);
  }

  function initializeMap() {
    var map = new google.maps.Map(document.getElementById('map'), {
      zoom: 11,
      center: {
        lat: 34.04924594193164,
        lng: -118.24104309082031
      }
    });

    var trafficLayer = new google.maps.TrafficLayer();
    trafficLayer.setMap(map);

    var locations = [
      [1, 34.04924594193164, -118.24104309082037, 34.01451794193164, -118.497524309082039, ['Sensor Node 1',
        '10.252.13.10', '10.252.12.255', 'Static', domainAddress + '/images/Sensor.png'
      ]],
      [2, 34.02529574193164, -118.16932409082031,34.04924594193366, -118.24104309082031, ['Sensor Node 2',
        '10.252.13.36', '10.252.12.255', 'Static', domainAddress + '/images/Sensor.png'
      ]],
      [3, 34.0258754193165, -118.395086109082031, 34.04924594193164, -118.24104309082037, ['Sensor Node 3',
        '10.252.13.40', '10.252.12.255', 'Static', domainAddress + '/images/Sensor.png'
      ]]
    ];

    //var driverProfile = [['Vehical 1','Bob Doe','30','2010/06/20','UP32 hy 666'],['Vehical 2','Jim Doe','36','2016/06/21','UP32 hn 532'],['Vehical 3','Phill Doe','30','2010/06/20','UP32 hp 321']]

    var marker, i;

    for (i = 0; i < locations.length; i++) {

      GeneratePoints(locations[i][0], map, new google.maps.LatLng(locations[i][1], locations[i][2]), new google.maps
        .LatLng(locations[
          i][3], locations[i][4]), locations[i][5]);

    }


  }

  function GeneratePoints(id, map, startPos, endPos, profile) {

    // generate fake points for route
    var pointsNo = 1000;
    var latDelta = (endPos.lat() - startPos.lat()) / pointsNo;
    var lngDelta = (endPos.lng() - startPos.lng()) / pointsNo;
    var positions = [];
    var markers = [];

    for (var i = 0; i < pointsNo; i++) {
      var curLat = startPos.lat() + i * latDelta;
      var curLng = startPos.lng() + i * lngDelta;
      positions.push(new google.maps.LatLng(curLat, curLng));


      var curMarker = new google.maps.Marker({
        map: map,
        position: new google.maps.LatLng(curLat, curLng),
        visible: false,
        title: profile[0],
        icon: 'http://maps.google.com/mapfiles/kml/pal4/icon23.png'
      });
      $('.loader').hide();

      curMarker.addListener('click', function () {
        
        $('.hTitle').html(profile[0]);
        $('.userName').html(profile[1]);
        //$('#age').html(profile[1]);
        $('#joinedOn').html(profile[2]);
        $('#vehicleNo').html(profile[3]);
        $('#avatar').attr('src', profile[4]);
        $('#chart_div').hide();
        $('#alertChart').hide();
        $('.loader').show();
        $('#alertGraph').hide();
        $('#alertGraph1').hide();
        $('#vehicleId').val(id);

        setTimeout(function () {
          $('.loader').hide();
          $('#chart_div').show();
          $('#alertChart').show();
          $('#alertGraph').show();
          $('#alertGraph1').show();
        }, 2000);

      });
      markers.push(curMarker);
    }

    displayMarker(markers, 0, delay);
  }


  function displayMarker(markers, index, delay) {
    if (index > 0)
      markers[index - 1].setVisible(false);
    else {
      markers[markers.length - 1].setVisible(false);
    }

    markers[index].setVisible(true);
    if (index < markers.length - 1) {
      setTimeout(function () {
        displayMarker(markers, index + 1, delay);
      }, delay);
    } else {
      displayMarker(markers, 0, delay);
    }
  }

  function updateChart(data) {
    var lineData = {
      date: new Date(data.eventTime),
      speed: data.vehicleSpeed
      /*  y: data.vehicleHeat,
       z: data.vehicleHumidity */
    };

    let now = new Date();
    dta[0].values.push(lineData);
    var lineData1 = {
      date: new Date(data.eventTime),
      speed: data.vehicleHeat
      /*  y: data.vehicleHeat,
       z: data.vehicleHumidity */
    };
    dta[1].values.push(lineData1);
    var lineData2 = {
      date: new Date(data.eventTime),
      speed: data.vehicleHumidity
      /*  y: data.vehicleHeat,
       z: data.vehicleHumidity */
    };
    dta[2].values.push(lineData2);


    // Shift domain
    x1.domain([now - ((limit - 2) * duration), now - duration])
    // Slide x-axis left
    x_axis_svg.transition().duration(duration).ease(d3.easeLinear, 2).call(x_axis);

    //Join
    var minerG = pathsG.selectAll(".minerLine").data(dta);
    var minerGEnter = minerG.enter()
      //Enter
      .append("g")
      .attr("class", "minerLine")
      .merge(minerG);

    //Join
    var minerSVG = minerGEnter.selectAll("path").data(function (d) {
      return [d];
    });

    var line = d3.line()
      .curve(d3.curveBasis)
      .x(function (d) {
        return x1(d.date);
      })
      .y(function (d) {
        return y1(d.speed);
      });
    var minerSVGenter = minerSVG.enter()
      //Enter
      .append("path").attr("class", "line")
      .style("stroke", function (d) {
        return z1(d.id);
      })
      .merge(minerSVG)
      //Update
      .transition()
      .duration(duration)
      .ease(d3.easeLinear, 2)
      .attr("d", function (d) {
        return line(d.values)
      })
      .attr("transform", null)

    var minerText = d3.select("#legend").selectAll("div").data(dta)
    var minerEnter = minerText.enter()
      .append("div")
      .attr("class", "legenditem")
      .style("color", function (d) {
        return z1(d.id);
      })
      .merge(minerText)
      .text(function (d) {
        return d.id + ":" + d.values[d.values.length - 1].speed;
      })

  }
  
  function updateChartE(data) {
    var lineData = {
      date: new Date(data.eventTime),
      speed: data.vehicleCO
      /*  y: data.vehicleHeat,
       z: data.vehicleHumidity */
    };

    let now = new Date();
    dtaE[0].values.push(lineData);
    var lineData1 = {
      date: new Date(data.eventTime),
      speed: data.vehicleCO2
      /*  y: data.vehicleHeat,
       z: data.vehicleHumidity */
    };
    dtaE[1].values.push(lineData1);
    var lineData2 = {
      date: new Date(data.eventTime),
      speed: data.vehicleNO2
      /*  y: data.vehicleHeat,
       z: data.vehicleHumidity */
    };
    dtaE[2].values.push(lineData2);
  


    // Shift domain
    x1E.domain([now - ((limit - 2) * duration), now - duration])
    // Slide x-axis left
    x_axis_svgE.transition().duration(duration).ease(d3.easeLinear, 2).call(x_axisE);

    //Join
    var minerG = pathsGE.selectAll(".minerLine").data(dtaE);
    var minerGEnter = minerG.enter()
      //Enter
      .append("g")
      .attr("class", "minerLine")
      .merge(minerG);

    //Join
    var minerSVG = minerGEnter.selectAll("path").data(function (d) {
      return [d];
    });

    var line = d3.line()
      .curve(d3.curveBasis)
      .x(function (d) {
        return x1E(d.date);
      })
      .y(function (d) {
        return y1E(d.speed);
      });
    var minerSVGenter = minerSVG.enter()
      //Enter
      .append("path").attr("class", "line")
      .style("stroke", function (d) {
        return z1E(d.id);
      })
      .merge(minerSVG)
      //Update
      .transition()
      .duration(duration)
      .ease(d3.easeLinear, 2)
      .attr("d", function (d) {
        return line(d.values)
      })
      .attr("transform", null)

    var minerText = d3.select("#legend1").selectAll("div").data(dtaE)
    var minerEnter = minerText.enter()
      .append("div")
      .attr("class", "legenditem")
      .style("color", function (d) {
        return z1E(d.id);
      })
      .merge(minerText)
      .text(function (d) {
        return d.id + ":" + d.values[d.values.length - 1].speed;
      })

  }