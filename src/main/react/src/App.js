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
        return response.text();
      })
      .then(text => {
        this.setState({
          text: text
        });
      });
  }

  render() {
    return <CalendarContainer />;
  }
}

export default App;
