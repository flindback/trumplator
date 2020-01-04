import React, { Component } from "react";
import Calendar from "../Calendar/Calendar.js";

const generateWindows = () => {
  const NUMBER_OF_DAYS = 24;
  const windows = [];
  for (let i = 0; i < NUMBER_OF_DAYS; i++) {
    const element = {
      label: i + 1,
      isOpen: false
    };
    windows.push(element);
  }
  console.log(windows);
  return windows;
};

class CalendarContainer extends Component {
  constructor(props) {
    super(props);
    this.state = {
      windows: []
    };
    this.openWindow = this.openWindow.bind(this);
  }

  componentDidMount() {
    const windows = generateWindows();
    this.setState({
      windows
    });
  }

  openWindow(number) {
    const windows = [...this.state.windows];
    windows[number - 1].isOpen = true;
    this.setState({
      windows
    });
  }

  render() {
    return (
      <Calendar windows={this.state.windows} openWindow={this.openWindow} />
    );
  }
}

export default CalendarContainer;
