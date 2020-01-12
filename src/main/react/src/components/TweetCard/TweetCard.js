import React, { Component } from "react";
import Weather from "../Weather/Weather.js";

const IMAGES = {
  realDonaldTrump: [
    { path: "/img/trump-1.png", alt: "Trump looking smug." },
    { path: "/img/trump-2.png", alt: "Trump looking angry." },
    { path: "/img/trump-3.png", alt: "Trump looking frightened." }
  ],
  FLOTUS: [
    { path: "/img/melania-1.png", alt: "Melania Trump looking surprised." },
    { path: "/img/melania-2.png", alt: "Melania Trump looking sneaky." },
    { path: "/img/melania-3.png", alt: "Melania Trump looking suspicious." }
  ],
  donaldjtrumpjr: [
    {
      path: "/img/junior-1.png",
      alt: "Junior Trump looking into the distance."
    },
    { path: "/img/junior-2.png", alt: "Junior Trump looking slightly sad." },
    {
      path: "/img/junior-3.png",
      alt: "Junior Trump looking proud, but constipated."
    }
  ]
};

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      tweeters: []
    };
  }

  getImage(tweeter) {
    var number = Math.floor(Math.random() * 3);
    var image = IMAGES[tweeter][number];
    return image;
  }

  render() {
    const tweeter = this.props.tweeter;
    var image = this.getImage(tweeter.name);
    return (
      <div className="tweet-card">
        <div className="weather-box">
          <p className="weather-box__text">{tweeter.description}</p>
          <Weather weather={tweeter.weather} />
        </div>
        <img className="tweet-image" src={image.path} alt={image.alt} />
        <div className="tweet-card__content">
          <p className="tweeter-name">{tweeter.name}</p>
          <p className="tweet-text">{tweeter.text}</p>
        </div>
      </div>
    );
  }
}

export default App;
