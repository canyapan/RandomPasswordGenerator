/*
 * Copyright 2015 CAN YAPAN
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.canyapan.randompasswordgenerator;

import java.io.IOException;

/**
 * Created by Jan on 19.07.2015.
 */
public class PasswordMeterException extends IOException {
    PasswordMeterException() {
        super();
    }

    PasswordMeterException(String message) {
        super(message);
    }

    PasswordMeterException(String message, Throwable cause) {
        super(message, cause);
    }
}
