import React from 'react';

import {
  CastContextProps,
  CastStateProps,
  CastMediaProps,
} from '../types/CastContext';

import RnGoogleCast from '..';

export const CastContext = React.createContext<CastContextProps>({
  castMedia: async data => {},
  device: null,
  connected: false,
  currentMedia: null,
});

export function useCast() {
  return React.useContext(CastContext);
}

class CastProvider extends React.PureComponent {
  state = {
    device: null,
    connected: false,
    castStatus: '',
    title: 'Escolha uma mídia',
    img: null,
  };

  subscribe: any;
  media: any;

  componentDidMount() {
    RnGoogleCast.getCastState().then(this.setConnected);
  }

  setConnected = (data: CastStateProps) => {
    const isConnected = data === 'connected';
    this.setState({ isConnected, castStatus: data });

    this.updateDevice();
  };

  updateDevice = async () => {
    try {
      if (this.state.connected) {
        const data = await RnGoogleCast.getCastDevice();
        this.setState({ device: data });
      } else if (this.state.device !== null) {
        this.setState({ device: null });
      }
    } catch (error) {
      this.setState({ device: null });
    }
  };

  render() {
    return (
      <CastContext.Provider
        value={{
          device: this.state.device,
          connected: this.state.connected,
          currentMedia: null,
          castMedia: (data: CastMediaProps) => RnGoogleCast.castMedia(data),
        }}
      >
        {this.props.children}
      </CastContext.Provider>
    );
  }
}

export default CastProvider;
