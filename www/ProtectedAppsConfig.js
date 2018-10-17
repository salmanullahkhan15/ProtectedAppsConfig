var ProtectedAppsConfig = function() {
};

ProtectedAppsConfig.open = function(setting, onsucess, onfail) {
	var settings = (typeof setting === 'string' || setting instanceof String) ? [setting] : setting;
	cordova.exec(onsucess, onfail, "ProtectedAppsConfig", "open", settings);
};

module.exports = ProtectedAppsConfig;