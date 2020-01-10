import React, { Component } from "react";
import "./App.css";
import CalendarContainer from "./components/CalendarContainer/CalendarContainer";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      text: ""
    };
  }

  componentDidMount() {
    fetch("http://localhost:5000/")
      .then(response => {
        return response.json();
      })
      .then(data => {
        this.setState({
          text: data.translation,
          weather: data.main
        });
      });
  }

  render() {
    return <CalendarContainer text={this.state.text} />;
    //return <p>{this.state.text}</p>;
  }
}

export default App;
