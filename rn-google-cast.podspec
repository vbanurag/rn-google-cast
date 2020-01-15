require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = package["name"]
  s.version      = package["version"]
  s.summary      = package["description"]

  s.homepage     = "https://github.com/github_account/rn-google-cast"
  s.license      = "MIT"
  # s.license    = { :type => "MIT", :file => "FILE_LICENSE" }
  s.authors      = { "Joao Alves" => "b3coded@gmail.com" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/b3coded/rn-google-cast.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,swift}"
  s.default_subspec = 'NoBluetooth'
  s.requires_arc = true

  s.dependency "React"

  s.subspec 'Default' do |ss|
    ss.dependency "#{package['name']}/RnGoogleCast"
    ss.dependency 'google-cast-sdk', '<= 4.3.0'
  end
  s.subspec 'NoBluetooth' do |ss|
    ss.dependency "#{package['name']}/RnGoogleCast"
    ss.dependency 'google-cast-sdk-no-bluetooth'
  end
  s.subspec 'Manual' do |ss|
    ss.dependency "#{package['name']}/RnGoogleCast"
  end
  s.subspec 'RnGoogleCast' do |ss|
    ss.source_files = "RnGoogleCast/**/*.{h,m}"
    ss.dependency 'PromisesObjC'
  end
end

