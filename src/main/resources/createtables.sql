/*
 * This file is part of BungeeQ, licensed under the MIT License.
 *
 *  Copyright (c) 2020 Maurice Hildebrand <webadmin@nevermined.de>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

-- -----------------------------------------------------
-- Table `bq_unlocks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bq_unlocks`
(
    id             int                                                     NOT NULL AUTO_INCREMENT,
    target_uuid    char(36)                                                NOT NULL,
    unlocker_uuid  char(36)                                                NOT NULL,
    start_datetime datetime                                                NOT NULL,
    end_datetime   datetime                                                NOT NULL,
    status         enum ('running', 'successful', 'cancelled', 'declined') NOT NULL,
    notice         text                                                    NOT NULL,
    PRIMARY KEY (id)
);


-- -----------------------------------------------------
-- Table `bq_logs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bq_logs`
(
    unlock_id int NOT NULL,
    log       text,
    PRIMARY KEY (unlock_id),
    FOREIGN KEY (unlock_id) REFERENCES `bq_unlocks` (id) ON DELETE CASCADE
);
