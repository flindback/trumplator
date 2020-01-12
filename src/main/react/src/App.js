import React, { Component } from "react";
import "./App.css";
import TweetCard from "./components/TweetCard/TweetCard.js";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      tweeters: []
    };
  }

  getTweet(tweeter, location) {
    fetch(`http://localhost:5000?t=${tweeter}&c=${location}`)
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
              description: data.description
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
        </div>
        <button id="refresh-button" onClick={this.getTweets.bind(this)}>
          <i className="fas fa-redo"></i>
        </button>
      </>
    );
  }
}

export default App;
