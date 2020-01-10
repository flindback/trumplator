import React, { Component } from "react";
import "./Calendar.css";

class Calendar extends Component {
  render() {
    return (
      <div className="calendar">
        {this.props.windows.map(window => {
          const windowStateClass = window.isOpen ? "is-open" : "is-closed";
          return (
            <div className="calendar-container">
              <div
                className={`calendar-window ${windowStateClass}`}
                id={window.label}
                onClick={() => {
                  this.props.openWindow(window.label);
                }}
              >
                {window.isOpen ? "" : window.label}
              </div>
              <div
                className={
                  window.isOpen
                    ? "window-content-open"
                    : "window-content-closed"
                }
              >
                <p>{this.props.text}</p>
              </div>
            </div>
          );
        })}
      </div>
    );
  }
}

export default Calendar;
