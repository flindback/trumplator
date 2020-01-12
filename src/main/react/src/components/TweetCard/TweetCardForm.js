import React, { Component } from "react";
import Weather from "../Weather/Weather.js";

const IMAGES = {};

class TweetCardForm extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  getImage() {
    var number = Math.floor(Math.random() * 7);
    var image = {
      path: "/img/Yoda/yoda-" + number + ".png",
      alt: "Picture of Yoda from Star Wars."
    };
    return image;
  }

  getData() {
    const userField = document.querySelector(".tweeter-name__input");
    const cityField = document.querySelector(".weather-box__input");
    const user = userField.value;
    const city = cityField.value;
    this.props.getTweet(user, city);
  }
  render() {
    const image = this.getImage();
    return (
      <div className="tweet-card">
        <div className="weather-box">
          <input
            type="text"
            placeholder="city..."
            className="weather-box__input"
          ></input>
          <i className="fas fa-question"></i>
        </div>
        <img className="tweet-image" src={image.path} alt={image.alt} />
        <div className="tweet-card__content">
          <input
            type="text"
            placeholder="tweeter name..."
            className="tweeter-name__input"
          ></input>
          <p className="tweet-text">Choose a tweeter!</p>
        </div>
        <button
          className="submit-button"
          type="button"
          onClick={this.getData.bind(this)}
        >
          submit
        </button>
      </div>
    );
  }
}

export default TweetCardForm;
