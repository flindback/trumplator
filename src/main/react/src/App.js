import React, { Component } from "react";
import "./App.css";
import TweetCard from "./components/TweetCard/TweetCard.js";
import TweetCardForm from "./components/TweetCard/TweetCardForm.js";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      tweeters: []
    };
  }

  getTweet(tweeter, location) {
    const defaultTweeter = tweeter.length ? tweeter : "realDonaldTrump";
    const defaultLocation = location.length ? location : "washington";
    fetch(`http://localhost:5000?t=${defaultTweeter}&c=${defaultLocation}`)
      .then(response => {
        return response.json();
      })
      .then(data => {
        this.setState({
          tweeters: [
            ...this.state.tweeters,
            {
              name: tweeter,
              text: data.translation,
              weather: data.main,
              description: data.description,
              image: data.profile_image_url
            }
          ]
        });
      });
  }

  getTweets() {
    this.setState({
      tweeters: []
    });
    this.getTweet("realDonaldTrump", "washington");
    this.getTweet("FLOTUS", "sevnica");
    this.getTweet("donaldjtrumpjr", "cairo");
  }

  componentDidMount() {
    this.getTweets();
  }

  render() {
    return (
      <>
        <div id="container">
          {this.state.tweeters.map((tweeter, i) => (
            <TweetCard key={i} tweeter={tweeter} />
          ))}
          <TweetCardForm getTweet={this.getTweet.bind(this)} />
        </div>
        <button id="refresh-button" onClick={this.getTweets.bind(this)}>
          <i className="fas fa-redo"></i>
        </button>
      </>
    );
  }
}

export default App;
