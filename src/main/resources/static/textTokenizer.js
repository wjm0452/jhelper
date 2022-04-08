function TextTokenizer(text, customToken) {

	let _text = text,
		_buff = text.split(''),
		_pos = -1,
		_customToken = {};

	if (customToken) {
		_customToken = customToken;
	}

	this.isAlphaNumeric = function (c) {

		if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
			c == '_'
		) {
			return true;
		}

		return false;
	};

	this.remaning = function () {
		return !(_pos == _buff.length - 1);
	};

	this.position = function (pos) {

		if (arguments.length == 0) {
			return _pos;
		}

		_pos += pos;
	};

	this.get = function (pos) {

		if (arguments.length == 0) {
			return _buff[++_pos];
		}

		return _buff[_pos + pos];
	};

	this.getString = function (pos, offset) {

		if (arguments.length == 1) {
			offset = pos;
			pos = 0;
		}

		pos += _pos;
		return _text.substring(pos, Math.min(pos + offset, _buff.length));
	};

	this.hasNext = function () {
		return this.remaning();
	};

	this.next = function () {

		for (var key in this._customToken) {

			let begin = key,
				end = this._customToken[key];

			let c = this.get(1);

			if (begin == this.getString(1, begin.length)) {

				let append = '';
				append += begin;
				this.position(begin.length);

				while (this.remaning()) {

					c = this.get();

					if (end.charAt(0) == c) {
						if (end == this.getString(end.length())) {
							break;
						}
					}

					append += c;
				}

				append += end;
				this.position(end.length - 1);
				return append;
			}

		}

		let append = '';

		do {

			let c = this.get();

			append += c;

			if (!this.isAlphaNumeric(c)) {
				break;
			}

			if (!this.remaning()) {
				break;
			}

		} while (this.isAlphaNumeric(this.get(1)));

		return append;
	};

}