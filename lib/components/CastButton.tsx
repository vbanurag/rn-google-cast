import React from 'react';
import { requireNativeComponent } from 'react-native';

import { CastButtonProps } from '../types/CastButton';

const RnCastButton = requireNativeComponent('RnCastButton');

const CastButton: React.FC<CastButtonProps> = props => {
  return <RnCastButton {...props} />;
};

export default CastButton;
