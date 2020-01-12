import React, { Component } from "react";

/*
<i class="fas fa-bolt"></i>
fas fa-cloud
fas fa-snowflake
fas fa-cloud-showers-heavy
fas fa-cloud-sun-rain
fas fa-skull-crossbones
*/

const DEFAULT_ICON = "skull-crossbones";
const weatherMap = {
  Thunderstorm: "bolt",
  Drizzle: "cloud-sun-rain",
  Clouds: "cloud",
  Rain: "cloud-showers-heavy",
  Clear: "sun",
  Snow: "snowflake"
};

class Weather extends Component {
  render() {
    const weather = weatherMap[this.props.weather];
    const weatherClass = weather || DEFAULT_ICON;
    return <i className={"fas fa-" + weatherClass}></i>;
  }
}

export default Weather;
