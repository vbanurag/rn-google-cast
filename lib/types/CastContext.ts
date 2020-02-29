export interface CastContextProps {
  device: CastDeviceProps | null;
  currentMedia: any;
  connected: boolean;
  castMedia: (media: CastMediaProps) => Promise<any>;
}

export interface CastMediaProps {}

export type CastStateProps =
  | 'notConnected'
  | 'connected'
  | 'connecting'
  | 'noDevicesAvailable';

export interface CastDeviceProps {
  id: string | number;
  version: string | number;
  name: string;
  model: string;
}
