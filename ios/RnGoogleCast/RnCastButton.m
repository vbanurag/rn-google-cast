#import <GoogleCast/GoogleCast.h>
#import "RnCastButton.h"

@implementation RnCastButton
{
  GCKUICastButton *_castButton;
  UIColor *_tintColor;
}

-(void)layoutSubviews {
  _castButton = [[GCKUICastButton alloc] initWithFrame:self.bounds];
  _castButton.tintColor = _tintColor;
  [self addSubview:_castButton];
}

-(void)setTintColor:(UIColor *)color {
  _tintColor = color;
  super.tintColor = color;
  [self setNeedsDisplay];
}

@end